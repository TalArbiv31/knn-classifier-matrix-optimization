import java.util.PriorityQueue;
import java.util.Comparator;

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
            double sigma = deviation(m, j);
            
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
     */
    public static Matrix squareDistance(Matrix m) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        
        int rows = m.getNumRows();
        Matrix ones = new FlatMatrix(rows, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                ones.set(i, j, 1.0);
            }
        }
        
        Matrix FT = m.transpose();
        Matrix FFT = multiply(m, FT);
        Matrix diagFFT = diagonal(FFT);
        
        Matrix firstTerm = multiply(ones, diagFFT);
        Matrix secondTerm = multiply(diagFFT, ones);
        Matrix thirdTerm = add(FFT, FFT);
        
        return sub(add(firstTerm, secondTerm), thirdTerm);
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
                double temp = mat1.get(i, k); //Cached
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
     * Performs a selection sort on an array of indices based on a custom comparator.
     */
    public static int[] selectionSort(int[] indices, Comparator<Integer> comp) {
        if (indices == null || comp == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        
        int n = indices.length;
        int[] ans = new int[n];
        System.arraycopy(indices, 0, ans, 0, n);
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comp.compare(ans[j], ans[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            int temp = ans[i];
            ans[i] = ans[minIndex];
            ans[minIndex] = temp;
        }
        return ans;
    }

    /**
     * Internal helper class to store a neighbor's index and its distance.
     */
    private static class Neighbor {
        int index;       // Row index of the historical training instance (used to retrieve the label)
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
        if (trainingFeatures == null || trainingClasses == null || testFeatures == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (trainingFeatures.getNumRows() == 0 || trainingFeatures.getNumCols() == 0 || 
            testFeatures.getNumRows() == 0 || testFeatures.getNumCols() == 0) {
            throw new RuntimeException("Matrix dimensions cannot be 0");
        }
        if (k < 1 || k > trainingFeatures.getNumRows()) {
            throw new IllegalArgumentException("k must be between 1 and the number of training instances");
        }
        
        Matrix normalizedTrain = normalize(trainingFeatures);
        Matrix normalizedTest = normalizeUsingTrainStats(testFeatures, trainingFeatures);
        
        int numTrain = normalizedTrain.getNumRows();
        int numTest = normalizedTest.getNumRows();
        int numCols = normalizedTrain.getNumCols();
        
        // Optimize distance calculation: Combine Test and Train matrices once.
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
        
        // Calculate the distance matrix (Rows 0 to numTest-1 are test instances, the rest are training instances)
        Matrix distMatrix = squareDistance(combined);
        boolean[] predictions = new boolean[numTest];
        
        for (int i = 0; i < numTest; i++) { // Iterate over each test instance
            
            // Create a Max-Heap. 
            // We reverse the comparison order so the neighbor with the *largest* distance among the k is at the head of the queue.
            PriorityQueue<Neighbor> maxHeap = new PriorityQueue<>(
                (n1, n2) -> Double.compare(n2.distance, n1.distance)
            );
            
            // Scan all historical training instances (finding the k nearest in O(N log k) time)
            for (int m = 0; m < numTrain; m++) {
                int trainRowInCombined = numTest + m;
                // The distance is located at cell (i, trainRowInCombined) in the distance matrix
                double dist = distMatrix.get(i, trainRowInCombined);
                
                maxHeap.offer(new Neighbor(m, dist));
                
                // If the heap size exceeds k, remove the neighbor with the largest distance
                if (maxHeap.size() > k) {
                    maxHeap.poll(); 
                }
            }
            
            // The heap now contains exactly the k nearest neighbors. 
            // Iterate through them to perform a majority vote.
            int trueVotes = 0;
            for (Neighbor neighbor : maxHeap) {
                if (trainingClasses[neighbor.index]) {
                    trueVotes++;
                }
            }
            
            // Majority vote logic (valid for binary classification where k is odd)
            predictions[i] = (trueVotes > k / 2.0);
        }
        
        return predictions;
    }

    /**
     * Calculates the accuracy of the predictions against the ground truth labels.
     */
    public static double measureAccuracy(boolean[] testLabels, boolean[] trueLabels) {
        if (testLabels == null || trueLabels == null || testLabels.length == 0 || testLabels.length != trueLabels.length) {
            return 0;
        }
        
        int correct = 0;
        int totalObjects = testLabels.length;
        for (int i = 0; i < totalObjects; i++) {
            if (testLabels[i] == trueLabels[i]) {
                correct++;
            }
        }
        return (double) correct / totalObjects;
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
     */
    private static double deviation(Matrix m, int col) {
        double mu = mu(m, col);
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
            double sigma = deviation(train, j);
            
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
        return ans;
    }
}
