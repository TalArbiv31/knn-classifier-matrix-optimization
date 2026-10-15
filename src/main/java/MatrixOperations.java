import java.util.PriorityQueue;

/**
 * Provides static utility methods for matrix operations and k-NN classification computations.
 */
public class MatrixOperations {

    /**
     * Extracts a sub-matrix from the given matrix starting from row 'from' (inclusive) to 'to' (exclusive).
     */
    public static Matrix subMatrix(Matrix mat, int from, int to) {
        if (mat == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if (from < 0 || to <= from || to > mat.getNumRows()) {
            throw new IllegalArgumentException("Invalid values for from and to");
        }
        
        Matrix ans = new FlatMatrix(to - from, mat.getNumCols());
        for (int i = from; i < to; i++) {
            for (int j = 0; j < mat.getNumCols(); j++) {
                ans.set(i - from, j, mat.get(i, j));
            }
        }
        return ans;
    }

    /**
     * Normalizes the matrix features using Z-score normalization (Mean=0, StdDev=1).
     */
    public static Matrix normalize(Matrix m) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if (m.getNumRows() == 0) {
            throw new IllegalArgumentException("Matrix must have at least one row");
        }
        
        int rows = m.getNumRows();
        int cols = m.getNumCols();
        Matrix ans = new FlatMatrix(rows, cols);
        
        for (int j = 0; j < cols; j++) {
            double mu = mu(m, j);
            double sigma = deviation(m, j, mu); // Optimized: pass mu directly
            
            if (sigma == 0) {
                for (int i = 0; i < rows; i++) {
                    ans.set(i, j, 0.0);
                }
            } else {
                for (int i = 0; i < rows; i++) {
                    double normalizedVal = (m.get(i, j) - mu) / sigma;
                    ans.set(i, j, normalizedVal);
                }
            }
        }
        MatrixUtils.roundMatrix(ans);
        return ans;
    }

    /**
     * Computes the squared Euclidean distance matrix for the given feature matrix.
     * Optimized to run in O(N^2 * D) time and O(N^2) space instead of O(N^3),
     * avoiding redundant intermediate matrix allocations and broadcasting multiplications.
     *
     * @param m The feature matrix (N x D)
     * @return A square distance matrix (N x N) where cell (i, j) contains ||x_i - x_j||^2
     */
    public static Matrix squareDistance(Matrix m) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        
        int rows = m.getNumRows();
        
        // 1. Compute dot products between all row pairs (X * Y^T)
        // m is (N x D) and its transpose is (D x N); multiplication takes O(N^2 * D)
        Matrix FT = m.transpose();
        Matrix FFT = multiply(m, FT);
        
        // 2. Extract squared norms for each instance (||x_i||^2)
        // These values lie along the diagonal of FFT. Storing them in a 1D array takes O(N)
        double[] diagValues = new double[rows];
        for (int i = 0; i < rows; i++) {
            diagValues[i] = FFT.get(i, i);
        }
        
        // 3. Construct the pairwise squared distance matrix directly
        // Eliminates intermediate matrix allocations (ones, firstTerm, secondTerm, etc.)
        // Evaluates the algebraic identity: ||x - y||^2 = ||x||^2 + ||y||^2 - 2<x, y> in O(N^2)
        Matrix distMatrix = new FlatMatrix(rows, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                double dist = diagValues[i] + diagValues[j] - 2 * FFT.get(i, j);
                // Prevent negative zeroes due to floating point inaccuracies
                if (dist < 0) dist = 0.0;
                distMatrix.set(i, j, dist);
            }
        }
        
        return distMatrix;
    }

    /**
     * Computes the matrix multiplication of mat1 and mat2.
     */
    public static Matrix multiply(Matrix mat1, Matrix mat2) {
        if (mat1 == null || mat2 == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (mat1.getNumCols() != mat2.getNumRows()) {
            throw new IllegalArgumentException("Incompatible dimensions for multiplication.");
        }
        
        Matrix ans = new FlatMatrix(mat1.getNumRows(), mat2.getNumCols());
        for (int i = 0; i < mat1.getNumRows(); i++) {
            for (int k = 0; k < mat1.getNumCols(); k++) {
                double temp = mat1.get(i, k); // Cached for cache-locality
                for (int j = 0; j < mat2.getNumCols(); j++) {
                    double currentSum = ans.get(i, j);
                    ans.set(i, j, currentSum + temp * mat2.get(k, j));
                }
            }
        }
        return ans;
    }

    /**
     * Returns a diagonal matrix based on the diagonal elements of the input square matrix.
     */
    public static Matrix diagonal(Matrix m) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (!m.isSquare()) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        
        Matrix ans = new FlatMatrix(m.getNumRows(), m.getNumCols());
        for (int i = 0; i < m.getNumRows(); i++) {
            ans.set(i, i, m.get(i, i));
        }
        return ans;
    }

    /**
     * Adds two matrices element-wise.
     */
    public static Matrix add(Matrix m1, Matrix m2) {
        if (m1 == null || m2 == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (m1.getNumRows() != m2.getNumRows() || m1.getNumCols() != m2.getNumCols()) {
            throw new IllegalArgumentException("Matrix dimensions must be compatible for addition.");
        }
        
        Matrix ans = new FlatMatrix(m1.getNumRows(), m1.getNumCols());
        for (int i = 0; i < m1.getNumRows(); i++) {
            for (int j = 0; j < m1.getNumCols(); j++) {
                ans.set(i, j, m1.get(i, j) + m2.get(i, j));
            }
        }
        return ans;
    }

    /**
     * Subtracts m2 from m1 element-wise.
     */
    public static Matrix sub(Matrix m1, Matrix m2) {
        if (m1 == null || m2 == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (m1.getNumRows() != m2.getNumRows() || m1.getNumCols() != m2.getNumCols()) {
            throw new IllegalArgumentException("Matrix dimensions must be compatible for subtraction.");
        }
        
        Matrix ans = new FlatMatrix(m1.getNumRows(), m1.getNumCols());
        for (int i = 0; i < m1.getNumRows(); i++) {
            for (int j = 0; j < m1.getNumCols(); j++) {
                ans.set(i, j, m1.get(i, j) - m2.get(i, j));
            }
        }
        return ans;
    }

    /**
     * Internal helper class to store a neighbor's index and its distance.
     */
    private static class Neighbor {
        int index;       // Row index of the historical training instance
        double distance; // Distance from the test instance

        public Neighbor(int index, double distance) {
            this.index = index;
            this.distance = distance;
        }
    }
    
    /**
     * Executes the k-NN classification algorithm to predict labels for the test features 
     * using a Max-Heap (PriorityQueue) for optimal O(N log k) performance.
     */
    public static boolean[] testClasses(Matrix trainingFeatures, boolean[] trainingClasses, Matrix testFeatures, int k) {
        // 1. Guard clauses (Security & Validation)
        if (trainingFeatures == null || trainingClasses == null || testFeatures == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (trainingFeatures.getNumRows() == 0 || trainingFeatures.getNumCols() == 0 || 
            testFeatures.getNumRows() == 0 || testFeatures.getNumCols() == 0) {
            throw new IllegalArgumentException("Matrix dimensions cannot be 0");
        }
        if (trainingFeatures.getNumRows() != trainingClasses.length) {
            throw new IllegalArgumentException("Number of training features rows must match labels array length");
        }
        if (trainingFeatures.getNumCols() != testFeatures.getNumCols()) {
            throw new IllegalArgumentException("Training and test features must have the same number of columns");
        }
        if (k < 1 || k > trainingFeatures.getNumRows()) {
            throw new IllegalArgumentException("k must be between 1 and the number of training instances");
        }
        
        // 2. Normalization
        Matrix normalizedTrain = normalize(trainingFeatures);
        Matrix normalizedTest = normalizeUsingTrainStats(testFeatures, trainingFeatures);
        
        int numTrain = normalizedTrain.getNumRows();
        int numTest = normalizedTest.getNumRows();
        int numCols = normalizedTrain.getNumCols();
        
        // 3. Optimize distance calculation: Combine Test and Train matrices once.
        Matrix combined = new FlatMatrix(numTest + numTrain, numCols);
        
        for (int row = 0; row < numTest; row++) {
            for (int col = 0; col < numCols; col++) {
                combined.set(row, col, normalizedTest.get(row, col));
            }
        }
        for (int row = 0; row < numTrain; row++) {
            for (int col = 0; col < numCols; col++) {
                combined.set(numTest + row, col, normalizedTrain.get(row, col));
            }
        }
        
        // 4. Calculate the distance matrix (Rows 0 to numTest-1 are test instances, the rest are training instances)
        Matrix distMatrix = squareDistance(combined);
        boolean[] predictions = new boolean[numTest];
        
        // 5. Find k-Nearest Neighbors
        for (int i = 0; i < numTest; i++) { 
            
            // Max-Heap: The neighbor with the *largest* distance among the k is at the head
            PriorityQueue<Neighbor> maxHeap = new PriorityQueue<>(
                (n1, n2) -> Double.compare(n2.distance, n1.distance)
            );
            
            // Scan all historical training instances (finding the k nearest in O(N log k) time)
            for (int m = 0; m < numTrain; m++) {
                int trainRowInCombined = numTest + m;
                // The distance is located at cell (i, trainRowInCombined) in the distance matrix
                double dist = distMatrix.get(i, trainRowInCombined);
                
                // Optimized Heap Insertion: Avoid unnecessary object creation and O(log k) operations
                if (maxHeap.size() < k) {
                    maxHeap.offer(new Neighbor(m, dist));
                } else if (dist < maxHeap.peek().distance) {
                    maxHeap.poll(); 
                    maxHeap.offer(new Neighbor(m, dist));
                }
            }
            
            // Majority vote logic
            int trueVotes = 0;
            for (Neighbor neighbor : maxHeap) {
                if (trainingClasses[neighbor.index]) {
                    trueVotes++;
                }
            }
            predictions[i] = (trueVotes > k / 2.0);
        }
        
        return predictions;
    }

    // =========================================================================
    // Helper Functions
    // =========================================================================

    /**
     * Computes the mean (mu) of a specific column in the given matrix.
     */
    private static double mu(Matrix m, int col) {
        double sum = 0;
        for (int i = 0; i < m.getNumRows(); i++) {
            sum += m.get(i, col);
        }
        return sum / m.getNumRows();
    }

    /**
     * Computes the standard deviation (sigma) of a specific column in the given matrix.
     * Takes pre-calculated mu to avoid redundant computations.
     */
    private static double deviation(Matrix m, int col, double mu) {
        double sum = 0;
        for (int i = 0; i < m.getNumRows(); i++) {
            double diff = mu - m.get(i, col);
            sum += (diff * diff);
        }
        return Math.sqrt(sum / m.getNumRows());
    }

    /**
     * Normalizes the test matrix using the statistical properties (mean and std dev) of the training matrix.
     */
    private static Matrix normalizeUsingTrainStats(Matrix test, Matrix train) {
        int rows = test.getNumRows();
        int cols = test.getNumCols();
        Matrix ans = new FlatMatrix(rows, cols);
        
        for (int j = 0; j < cols; j++) {
            double mu = mu(train, j);
            double sigma = deviation(train, j, mu); // Optimized
            
            if (sigma == 0) {
                for (int i = 0; i < rows; i++) {
                    ans.set(i, j, 0.0);
                }
            } else {
                for (int i = 0; i < rows; i++) {
                    double val = (test.get(i, j) - mu) / sigma;
                    ans.set(i, j, val);
                }
            }
        }
        
        // Maintained consistency with training data rounding
        MatrixUtils.roundMatrix(ans);
        return ans;
    }
}
