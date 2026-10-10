

import java.util.Comparator;

public class MatrixOperations {

    // Task 1.1
    // Assumes: 'm' not null and from and to legal indices
    // Assumes: from < to
    // Returns: FlatMatrix starting from row 'from' to row 'to'
    public static Matrix subMatrix(Matrix mat, int from, int to) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if(mat == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if(from < 0 || to <= from || to > mat.getNumRows()) {
            throw new IllegalArgumentException("invalid values for from, and to");
        }
        ans = new FlatMatrix(to - from ,mat.getNumCols());
        for (int i = from; i < to; i++) {
            for (int j = 0; j < mat.getNumCols(); j++) {
                ans.set(i - from,j,mat.get(i,j));
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.2
    // Assumes: 'm' not null
    // Returns: the normalized matrix
    public static Matrix normalize(Matrix m) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if(m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if(m.getNumRows() == 0) {
            throw new IllegalArgumentException("Matrix must have at least one row");
        }
        int rows = m.getNumRows();
        int cols = m.getNumCols();
        ans = new FlatMatrix(rows,cols);
        for (int j = 0; j < cols; j++) {
            double mu = mu(m, j);
            double sigma = deviation(m, j);
            if (sigma == 0) {
                for (int i = 0; i < rows; i++) {
                    ans.set(i, j, 0.0); // מציבים 0 אם כל הערכים בעמודה זהים
                }
            }
            else {
                for (int i = 0; i < rows; i++) {
                    double normalizedVal = (m.get(i, j) - mu) / sigma;
                    ans.set(i, j, normalizedVal);
                }
            }
        }
        MatrixUtils.roundMatrix(ans);
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.3
    // Assumes: 'm' is not null
    // Returns: the square distance matrix of 'mat'
    public static Matrix squareDistance(Matrix m) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if(m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        int rows = m.getNumRows();
        Matrix zero = new FlatMatrix(rows, rows);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows; j++){
                zero.set(i, j, 1.0);
            }
        }
        Matrix FT =m.transpose();
        Matrix FFT = multiply(m, FT);
        Matrix diagFFT = diagonal(FFT);
        Matrix firstTerm = multiply(zero, diagFFT);
        Matrix secondTerm = multiply(diagFFT, zero);
        Matrix thirdTerm = add(FFT , FFT);
        ans = sub(add(firstTerm, secondTerm), thirdTerm);
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }



    // Assumes: 'mat1' and 'mat2' be able to multiply
    // Returns: the product of 'mat1' and 'mat2'
    public static Matrix multiply(Matrix mat1, Matrix mat2) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if(mat1 == null || mat2 == null){
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if(mat1.getNumCols() != mat2.getNumRows()){
            throw new IllegalArgumentException("The number of columns of m1 must be equal to the number of rows of m2.");
        }
        ans = new FlatMatrix(mat1.getNumRows(), mat2.getNumCols());
        for (int i = 0; i < mat1.getNumRows(); i++) {
            for (int j = 0; j < mat2.getNumCols(); j++) {
                double sum =0;
                for (int k = 0; k < mat1.getNumCols(); k++) {
                    sum = sum + mat1.get(i, k) * mat2.get(k, j);
                }
                ans.set(i, j, sum);
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }


    // Task 1.3A
    // Assumes: 'mat' not null
    // Returns: the diagonal matrix
    public static Matrix diagonal(Matrix m) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if(m == null){
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (!m.isSquare()) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        ans = new FlatMatrix(m.getNumRows(), m.getNumCols());
        for (int i = 0; i < m.getNumRows(); i++) {
            ans.set(i, i, m.get(i, i));
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.3B
    // Assumes: 'm1' and 'm2' be able to add
    // Returns: the sum of 'mat1' and 'mat2'
    public static Matrix add(Matrix m1, Matrix m2) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if (m1 == null || m2 == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (m1.getNumRows() != m2.getNumRows() || m1.getNumCols() != m2.getNumCols()) {
            throw new IllegalArgumentException("Matrix dimensions must be compatible for addition.");
        }
        ans = new FlatMatrix(m1.getNumRows(), m1.getNumCols());
        for (int i = 0; i < m1.getNumRows(); i++) {
            for (int j = 0; j < m1.getNumCols(); j++) {
                double sum = m1.get(i, j) + m2.get(i, j);
                ans.set(i, j, sum);
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.3C
    // Assumes: 'm1' and 'm2' be able to subtract
    // Returns: the subtraction of 'mat1' and 'mat2'
    public static Matrix sub(Matrix m1, Matrix m2) {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if (m1 == null || m2 == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (m1.getNumRows() != m2.getNumRows() || m1.getNumCols() != m2.getNumCols()) {
            throw new IllegalArgumentException("Matrix dimensions must be compatible for subtraction.");
        }
        ans = new FlatMatrix(m1.getNumRows(), m1.getNumCols());
        for (int i = 0; i < m1.getNumRows(); i++) {
            for (int j = 0; j < m1.getNumCols(); j++) {
                double diff = m1.get(i, j) - m2.get(i, j);
                ans.set(i, j, diff);
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.4
    // Assumes: 'indices' is an index array
    // returns the sorted indexes of indices based on comp
    public static int[] selectionSort(int[] indices, Comparator<Integer> comp) {
        int[] ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if (indices == null || comp == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        int n = indices.length;
        ans = new int[n];
        for (int i = 0; i < indices.length; i++) {
            ans[i] = indices[i];
        }
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
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.5
    // Assumes: 'trainingFeatures', trainingLabels', 'validationFeatures' npt be null
    // Assumes: compatibility between 'trainingFeatures' and 'trainingLabels'
    // Returns: the validation labels of the 'validationFeatures'
    public static boolean[] testClasses(Matrix trainingFeatures, boolean[] trainingClasses, Matrix testFeatures, int k) {
        boolean[] ans = null;
        // ---------------write your code BELOW this line only! ------------------
        if (trainingFeatures == null || trainingClasses == null || testFeatures == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (trainingFeatures.getNumRows() == 0 || trainingFeatures.getNumCols() == 0 || testFeatures.getNumRows() == 0 || testFeatures.getNumCols() == 0) {
            throw new RuntimeException("Matrix dimensions cannot be 0");
        }
        if (k < 1 || k > trainingFeatures.getNumRows()) {
            throw new IllegalArgumentException("k must be between 1 and n");
        }
        Matrix normalizedTrain = normalize(trainingFeatures);
        Matrix normalizedTest = normalizeUsingTrainStats(testFeatures, trainingFeatures);
        int numTest = normalizedTest.getNumRows();
        ans = new boolean[numTest];
        for (int i = 0; i < numTest; i++) {
            int[] sortedIndices = getSortedIndices(normalizedTrain, normalizedTest, i);
            int trueVotes = 0;
            for (int n = 0; n < k; n++) {
                int trainingIndex = sortedIndices[n] - 1;
                if (trainingClasses[trainingIndex]) {
                    trueVotes++;
                }
            }
            ans[i] = (trueVotes > k / 2.0);
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Task 1.6
    // Assumes: 'testLabels' and 'trueLables' are arrays of same length
    // Returns: the accuracy of the test
    public static double measureAccuracy(boolean[] testLabels, boolean[] trueLabels) {
        double ans = 0;
        // ---------------write your code BELOW this line only! ------------------
        if (testLabels == null || trueLabels == null || testLabels.length == 0) {
            return 0;
        }
        int correct = 0;
        int totalObjects = testLabels.length;
        for (int i = 0; i < totalObjects; i++) {
            if (testLabels[i] == trueLabels[i]) {
                correct++;
            }
        }
        ans = (double) correct / totalObjects;

        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    //Helper Functions//

    //מחזיר את הערך של mu לפי הנוסחה
    public static double mu(Matrix m, int col) {
        double sum = 0;
        for (int i = 0; i < m.getNumRows(); i++) {
            sum = sum + m.get(i, col);
        }
        return sum / m.getNumRows();
    }

    //מחזיר את הערך של הסטיית תקן לפי הנוסחה
    public static double deviation(Matrix m, int col) {
        double sum = 0;
        double mu = mu(m, col);
        for (int i = 0; i < m.getNumRows(); i++) {
            double diff = mu - m.get(i, col);
            sum = sum + (diff * diff);
        }
        return Math.sqrt(sum / m.getNumRows());
    }

    //מחזיר את מטריצת המבחן המנורמלת לפי הערכים של מטריצת האימון
    public static Matrix normalizeUsingTrainStats(Matrix test, Matrix train) {
        int rows = test.getNumRows();
        int cols = test.getNumCols();
        Matrix ans = new FlatMatrix(rows, cols);
        for (int j = 0; j < cols; j++) {
            double mu = mu(train, j);
            double sigma = deviation(train, j);
            if (sigma == 0) {
                for (int i = 0; i < rows; i++) ans.set(i, j, 0.0);
            } else {
                for (int i = 0; i < rows; i++) {
                    double val = (test.get(i, j) - mu) / sigma;
                    ans.set(i, j, val);
                }
            }
        }
        return ans;
    }

    //מחזיר מערך ממויין של השכנים הכי קרובים למופע שבבדיקה מתוך המטריצת מבחן
    public static int[] getSortedIndices(Matrix normalizedTrain, Matrix normalizedTest, int testRowIndex) {
        int numTrain = normalizedTrain.getNumRows();
        int numCols = normalizedTrain.getNumCols();
        Matrix combined = new FlatMatrix(numTrain + 1, numCols);
        for (int col = 0; col < numCols; col++) {
            combined.set(0, col, normalizedTest.get(testRowIndex, col));
        }
        for (int row = 0; row < numTrain; row++) {
            for (int col = 0; col < numCols; col++) {
                combined.set(row + 1, col, normalizedTrain.get(row, col));
            }
        }
        Matrix distMatrix = squareDistance(combined);
        int[] indices = new int[numTrain];
        for (int m = 0; m < numTrain; m++) indices[m] = m + 1;
        Comparator<Integer> comp = new IndexComparator(distMatrix, 0);
        indices = selectionSort(indices, comp);
        return indices;
    }

}
