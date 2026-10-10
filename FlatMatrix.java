
//Task 2
public class FlatMatrix implements Matrix {

    private double[] mat;
    private int rows;
    private int cols;


    // Requires: 'rows' > 0, 'cols' > 0
    // Constructs a FlatMatrix with the given rows and cols, with zeros in all cells
    public FlatMatrix(int rows, int cols) {
        // ---------------write your code BELOW this line only! ------------------
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.mat = new double[rows * cols];
        // ---------------write your code ABOVE this line only! ------------------
    }


    // Requires: 'mat' be a matrix of nxm
    // Constructs a FlatMatrix with the same values as 'mat'
    public FlatMatrix(Matrix mat) {
        // ---------------write your code BELOW this line only! ------------------
        if (mat == null) {
            throw new IllegalArgumentException("Input matrix cannot be null");
        }
        this.rows = mat.getNumRows();
        this.cols = mat.getNumCols();
        this.mat = new double[this.rows * this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.mat[i * this.cols + j] = mat.get(i, j);
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
    }


    // Requires: 'mat' is not null
    // Constructs a FlatMatrix with the same values as 'mat'
    public FlatMatrix(double[][] mat) {
        // ---------------write your code BELOW this line only! ------------------
        if (mat == null) {
            throw new IllegalArgumentException("Input matrix cannot be null");
        }
        this.rows = mat.length;
        this.cols = mat[0].length;
        this.mat = new double[this.rows * this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.mat[i * this.cols + j] = mat[i][j];
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
    }

    // Requires: 'row' and 'col' are legal indices in the matrix
    // Returns the value in the corresponding row and col in the matrix
    @Override
    public double get(int i, int j) {
        double ans = 0;
        // ---------------write your code BELOW this line only! ------------------
        if (i < 0 || i >= this.rows || j < 0 || j >= this.cols) {
            throw new IllegalArgumentException("Index (" + i + "," + j + ") is out of bounds");
        }
        ans = this.mat[i * this.cols + j];
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Requires: 'row' and 'col' are legal indices in the matrix
    // changes the value in the corresponding row and col in the matrix with 'value'
    @Override
    public void set(int i, int j, double value) {
        // ---------------write your code BELOW this line only! ------------------
        if (i < 0 || i >= this.rows || j < 0 || j >= this.cols) {
            throw new IllegalArgumentException("Index (" + i + "," + j + ") is out of bounds");
        }
        this.mat[i * this.cols + j] = value;
        // ---------------write your code ABOVE this line only! ------------------
    }

    // Returns the number of rows of the matrix
    @Override
    public int getNumRows() {
        int ans = 0;
        // ---------------write your code BELOW this line only! ------------------
        ans = this.rows;
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Returns the number of rows of the matrix
    @Override
    public int getNumCols() {
        int ans = 0;
        // ---------------write your code BELOW this line only! ------------------
        ans = this.cols;
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Returns true if the matrix is square
    @Override
    public Boolean isSquare() {
        boolean ans = false;
        // ---------------write your code BELOW this line only! ------------------
        ans = (this.rows == this.cols);
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Creates a deep copy of the matrix.
    // (a) the new matrix has the same numbers of
    // rows and columns as the creating matrix.
    // (b) The values in the corresponding cells are
    // the same.
    // (c) Changing a value in one matrix does not change the
    // other.
    @Override
    public Matrix copy() {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        ans = new FlatMatrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                ans.set(i, j, this.get(i, j));
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Creates a new matrix, which is the transpose of the
    // creating matrix.
    @Override
    public Matrix transpose() {
        Matrix ans = null;
        // ---------------write your code BELOW this line only! ------------------
        ans = new FlatMatrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                ans.set(j, i, this.get(i, j));
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }

    // Returns if 'other' equals the FlatMatrix
    @Override
    public boolean equals(Object other) {
        boolean ans = false;
        // ---------------write your code BELOW this line only! ------------------
        if (other instanceof Matrix) {
            Matrix otherMat = (Matrix) other;
            if (this.rows == otherMat.getNumRows() && this.cols == otherMat.getNumCols()) {
                ans = true;
                for (int i = 0; i < this.rows && ans; i++) {
                    for (int j = 0; j < this.cols && ans; j++) {
                        if (this.get(i, j) != otherMat.get(i, j)) {
                            ans = false;
                        }
                    }
                }
            }
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }


    @Override
    public String toString() {
        String ans = "";
        // ---------------write your code BELOW this line only! ------------------
        ans = "FlatMatrix " + this.rows + " X " + this.cols;
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }
}
