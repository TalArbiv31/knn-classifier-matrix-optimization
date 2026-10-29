import java.util.Random;

public class PerformanceBenchmark {

    // Matrix dimensions large enough to ensure data exceeds L1/L2 cache boundaries
    private static final int SIZE = 1000;
    private static final long RANDOM_SEED = 42L;

    // Increased iterations for fair JVM benchmarking
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASURE_ITERATIONS = 10;

    public static void main(String[] args) {
        System.out.println("--- JIT Compiler Aware Benchmark: Cache Locality ---");

        System.out.println("[*] Generating " + SIZE + "x" + SIZE + " matrices...");
        double[][] rawData1 = generateRandomData(RANDOM_SEED);
        double[][] rawData2 = generateRandomData(RANDOM_SEED + 1);

        // Instantiate both implementations (contiguous 1D vs fragmented 2D array)
        Matrix flatMat1 = createFlatMatrix(rawData1);
        Matrix flatMat2 = createFlatMatrix(rawData2);

        Matrix stdMat1 = new Standard2DMatrix(rawData1);
        Matrix stdMat2 = new Standard2DMatrix(rawData2);

        // ==========================================
        // Phase 1: JIT Warm-up (No time measurement)
        // ==========================================
        System.out.println("[*] Starting JIT Warm-up (" + WARMUP_ITERATIONS + " iterations)...");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            MatrixOperations.multiply(stdMat1, stdMat2);
            MatrixOperations.multiply(flatMat1, flatMat2);
        }
        System.out.println("[*] Warm-up complete.\n");

        // ==========================================
        // Phase 2: Performance Measurement (Average Time)
        // ==========================================
        System.out.println("[*] Starting Benchmark (" + MEASURE_ITERATIONS + " iterations)...");

        // 1. Benchmark Standard 2D Array (non-contiguous memory, pointer chasing)
        long start2D = System.nanoTime();
        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            MatrixOperations.multiply(stdMat1, stdMat2);
        }
        long end2D = System.nanoTime();
        double avgTime2D = (end2D - start2D) / 1_000_000.0 / MEASURE_ITERATIONS;

        // 2. Benchmark FlatMatrix (contiguous memory, spatial locality friendly)
        long startFlat = System.nanoTime();
        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            MatrixOperations.multiply(flatMat1, flatMat2);
        }
        long endFlat = System.nanoTime();
        double avgTimeFlat = (endFlat - startFlat) / 1_000_000.0 / MEASURE_ITERATIONS;

        // ==========================================
        // Phase 3: Output comparative analysis
        // ==========================================
        System.out.println("\n--- Benchmark Results ---");
        System.out.printf("Standard 2D Array Average Time:  %.2f ms%n", avgTime2D);
        System.out.printf("FlatMatrix Average Time:         %.2f ms%n", avgTimeFlat);

        if (avgTimeFlat < avgTime2D) {
            double speedup = avgTime2D / avgTimeFlat;
            System.out.printf("\nSUCCESS: FlatMatrix is %.2fx faster due to Spatial Locality!%n", speedup);
        } else {
            System.out.println("\nNote: Try increasing matrix dimensions to amplify cache effects.");
        }
    }

    /**
     * Generates a 2D array populated with random double values using a fixed seed.
     */
    private static double[][] generateRandomData(long seed) {
        double[][] data = new double[SIZE][SIZE];
        Random rand = new Random(seed);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
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
                System.arraycopy(data[i], 0, copyData[i], 0, cols);
            }
            return new Standard2DMatrix(copyData);
        }
    }
}
