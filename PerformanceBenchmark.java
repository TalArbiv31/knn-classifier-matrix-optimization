import java.util.Random;

public class PerformanceBenchmark {

    public static void main(String[] args) {
        System.out.println("--- Performance Benchmark: Cache Locality ---");
        
        // הגדרת גודל מטריצה מספיק גדול כדי שהמעבד ירגיש את קפיצות הזיכרון
        int rows = 800; 
        int cols = 800;
        
        System.out.println("[*] Generating " + rows + "x" + cols + " matrices...");
        double[][] rawData1 = generateRandomData(rows, cols);
        double[][] rawData2 = generateRandomData(rows, cols);
        
        // יצירת המטריצות שלנו (הרציפה מול המפוצלת)
        Matrix flatMat1 = createFlatMatrix(rawData1);
        Matrix flatMat2 = createFlatMatrix(rawData2);
        
        Matrix stdMat1 = new Standard2DMatrix(rawData1);
        Matrix stdMat2 = new Standard2DMatrix(rawData2);

        System.out.println("[*] Warming up JVM (to ensure fair CPU test)...");
        // הרצה קטנה 'על ריק' כדי שה-JVM יקמפל את הקוד ל-Machine Code לפני המדידה האמיתית
        MatrixOperations.multiply(createFlatMatrix(new double[50][50]), createFlatMatrix(new double[50][50]));

        System.out.println("[*] Running Benchmark (Matrix Multiplication O(n^3))...\n");

        // 1. מדידת זמן למערך דו-ממדי רגיל (Cache Misses)
        long start2D = System.currentTimeMillis();
        MatrixOperations.multiply(stdMat1, stdMat2);
        long end2D = System.currentTimeMillis();
        long time2D = end2D - start2D;
        System.out.println("Standard 2D Array Matrix took: " + time2D + " ms");

        // 2. מדידת זמן למערך החד-ממדי שלך (Cache Friendly)
        long startFlat = System.currentTimeMillis();
        MatrixOperations.multiply(flatMat1, flatMat2);
        long endFlat = System.currentTimeMillis();
        long timeFlat = endFlat - startFlat;
        System.out.println("FlatMatrix (Contiguous Memory) took:  " + timeFlat + " ms");

        // 3. הדפסת שורת המחץ העסקית!
        System.out.println("\n--- Results ---");
        if (timeFlat < time2D) {
            double speedup = (double) time2D / timeFlat;
            System.out.printf("✅ SUCCESS: FlatMatrix is %.2fx faster due to Spatial Locality!%n", speedup);
        } else {
            System.out.println("Note: Try increasing the matrix size (e.g., 1000x1000) to see the cache effect.");
        }
    }

    // פונקציית עזר ליצירת נתונים אקראיים
    private static double[][] generateRandomData(int rows, int cols) {
        double[][] data = new double[rows][cols];
        Random rand = new Random(42); // Seed קבוע כדי שהתוצאות יהיו הדירות
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rand.nextDouble();
            }
        }
        return data;
    }

    // פונקציית עזר להמרת המערך ל-FlatMatrix מבלי להסתמך על בנאי מסוים
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

    /* 
     * מחלקה פנימית שנועדה לדמות את הדרך הגרועה לניהול זיכרון (מערך של מערכים).
     * היא מממשת את הממשק שלך כדי שנוכל לשלוח אותה לאותה פונקציית Multiply בדיוק!
     */
    static class Standard2DMatrix implements Matrix {
        private double[][] data;

        public Standard2DMatrix(double[][] data) {
            this.data = data;
        }

        @Override
        public int getNumRows() { return data.length; }

        @Override
        public int getNumCols() { return data[0].length; }

        @Override
        public double get(int i, int j) { return data[i][j]; }

        @Override
        public void set(int i, int j, double val) { data[i][j] = val; }

        @Override
        public boolean isSquare() { return getNumRows() == getNumCols(); }

        @Override
        public Matrix transpose() { 
            return null; // לא רלוונטי לבנצ'מרק הזה
        }
    }
}
