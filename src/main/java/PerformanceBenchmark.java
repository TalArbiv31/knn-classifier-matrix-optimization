import java.util.Random;

public class PerformanceBenchmark {

    // Matrix dimensions large enough to emphasize CPU cache misses
    private static final int ROWS = 800;
    private static final int COLS = 800;
    private static final long RANDOM_SEED = 42L;

    public static void main(String[] args) {
        System.out.println("--- Performance Benchmark: Cache Locality ---");

        System.out.println("[*] Generating " + ROWS + "x" + COLS + " matrices...");
        double[][] rawData1 = generateRandomData(ROWS, COLS, RANDOM_SEED);
        double[][] rawData2 = generateRandomData(ROWS, COLS, RANDOM_SEED + 1);

        // Instantiate both implementations (contiguous 1D vs fragmented 2D array)
        Matrix flatMat1 = createFlatMatrix(rawData1);
        Matrix flatMat2 = createFlatMatrix(rawData2);

        Matrix stdMat1 = new Standard2DMatrix(rawData1);
        Matrix stdMat2 = new Standard2DMatrix(rawData2);

        // Warm up the JVM to trigger JIT compilation before benchmarking
        System.out.println("[*] Warming up JVM (to ensure fair JIT compilation)...");
        warmUpJvm();

        System.out.println("[*] Running Benchmark (Matrix Multiplication O(n^3))...\n");

        // 1. Benchmark Standard 2D Array (non-contiguous memory, pointer chasing)
        long start2D = System.nanoTime();
        MatrixOperations.multiply(stdMat1, stdMat2);
        long end2D = System.nanoTime();
        double time2DMs = (end2D - start2D) / 1_000_000.0;
        System.out.printf("Standard 2D Array Matrix took:  %.2f ms%n", time2DMs);

        // 2. Benchmark FlatMatrix (contiguous memory, spatial locality friendly)
        long startFlat = System.nanoTime();
        MatrixOperations.multiply(flatMat1, flatMat2);
        long endFlat = System.nanoTime();
        double timeFlatMs = (endFlat - startFlat) / 1_000_000.0;
        System.out.printf("FlatMatrix (Contiguous Memory) took: %.2f ms%n", timeFlatMs);

        // 3. Output comparative analysis
        System.out.println("\n--- Results ---");
        if (timeFlatMs < time2DMs) {
            double speedup = time2DMs / timeFlatMs;
            System.out.printf("SUCCESS: FlatMatrix is %.2fx faster due to Spatial Locality!%n", speedup);
        } else {
            System.out.println("Note: Try increasing matrix dimensions (e.g., 1000x1000) to amplify cache effects.");
        }
    }

    /**
     * Executes a small multiplication pass to warm up JIT compiler optimizations.
     */
    private static void warmUpJvm() {
        Matrix warmUpMat1 = createFlatMatrix(new double[60][60]);
        Matrix warmUpMat2 = createFlatMatrix(new double[60][60]);
        MatrixOperations.multiply(warmUpMat1, warmUpMat2);
    }

    /**
     * Generates a 2D array populated with random double values using a fixed seed.
     */
    private static double[][] generateRandomData(int rows, int cols, long seed) {
        double[][] data = new double[rows][cols];
        Random rand = new Random(seed);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rand.nextDouble();
            }
        }
        return data;
    }

    /**
     * Converts a standard 2D array into a FlatMatrix instance.
     */
    private static Matrix createFlatMatrix(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        Matrix flat = new FlatMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flat.set(i, j, data[i][j]);
            }
        }
        return flat;
    }

    /**
     * Inner class representing a conventional 2D array (array of arrays).
     * Used as a baseline for cache inefficiency (indirection & pointer jumping).
     */
    static class Standard2DMatrix implements Matrix {
        private final double[][] data;

        public Standard2DMatrix(double[][] data) {
            this.data = data;
        }

        @Override
        public int getNumRows() {
            return data.length;
        }

        @Override
        public int getNumCols() {
            return data[0].length;
        }

        @Override
        public double get(int i, int j) {
            return data[i][j];
        }

        @Override
        public void set(int i, int j, double val) {
            data[i][j] = val;
        }

        @Override
        public Boolean isSquare() {
            return getNumRows() == getNumCols();
        }

        @Override
        public Matrix transpose() {
            int rows = getNumRows();
            int cols = getNumCols();
            double[][] transposed = new double[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    transposed[j][i] = data[i][j];
                }
            }
            return new Standard2DMatrix(transposed);
        }

        @Override
        public Matrix copy() {
            int rows = getNumRows();
            int cols = getNumCols();
            double[][] copyData = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    copyData[i][j] = data[i][j];
                }
            }
            return new Standard2DMatrix(copyData);
        }
    }
}
