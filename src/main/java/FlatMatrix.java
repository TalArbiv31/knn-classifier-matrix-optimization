public class FlatMatrix implements Matrix {

    private double[] mat;
    private int rows;
    private int cols;

    // Constructs a FlatMatrix with the given rows and cols, with zeros in all cells.
    // Requires: 'rows' > 0, 'cols' > 0
    public FlatMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }
        this.rows = rows;
        this.cols = cols;
        this.mat = new double[rows * cols];
    }

    // Constructs a FlatMatrix with the same values as the provided Matrix 'mat'.
    // Requires: 'mat' is not null
    public FlatMatrix(Matrix mat) {
        if (mat == null) {
            throw new IllegalArgumentException("Input matrix cannot be null.");
        }
        this.rows = mat.getNumRows();
        this.cols = mat.getNumCols();
        this.mat = new double[this.rows * this.cols];
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.mat[i * this.cols + j] = mat.get(i, j);
            }
        }
    }

    // Constructs a FlatMatrix from a 2D double array.
    // Requires: 'mat' is not null and not empty.
    public FlatMatrix(double[][] mat) {
        if (mat == null) {
            throw new IllegalArgumentException("Input matrix cannot be null.");
        }
        if (mat.length == 0 || mat[0].length == 0) {
             throw new IllegalArgumentException("Input matrix cannot be empty.");
        }
        
        this.rows = mat.length;
        this.cols = mat[0].length;
        this.mat = new double[this.rows * this.cols];
        
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.mat[i * this.cols + j] = mat[i][j];
            }
        }
    }

    // Returns the value in the corresponding row and col in the matrix.
    // Requires: 'i' and 'j' are legal indices in the matrix.
    @Override
    public double get(int i, int j) {
        if (i < 0 || i >= this.rows || j < 0 || j >= this.cols) {
            throw new IllegalArgumentException("Index (" + i + "," + j + ") is out of bounds.");
        }
        return this.mat[i * this.cols + j];
    }

    // Changes the value in the corresponding row and col in the matrix with 'value'.
    // Requires: 'i' and 'j' are legal indices in the matrix.
    @Override
    public void set(int i, int j, double value) {
        if (i < 0 || i >= this.rows || j < 0 || j >= this.cols) {
            throw new IllegalArgumentException("Index (" + i + "," + j + ") is out of bounds.");
        }
        this.mat[i * this.cols + j] = value;
    }

    // Returns the number of rows of the matrix.
    @Override
    public int getNumRows() {
        return this.rows;
    }

    // Returns the number of columns of the matrix.
    @Override
    public int getNumCols() {
        return this.cols;
    }

    // Returns true if the matrix is square.
    @Override
    public Boolean isSquare() {
        return (this.rows == this.cols);
    }

    // Creates a deep copy of the matrix.
    @Override
    public Matrix copy() {
        Matrix ans = new FlatMatrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                ans.set(i, j, this.get(i, j));
            }
        }
        return ans;
    }

    // Creates a new matrix, which is the transpose of the creating matrix.
    @Override
    public Matrix transpose() {
        Matrix ans = new FlatMatrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                ans.set(j, i, this.get(i, j));
            }
        }
        return ans;
    }

    // Returns true if 'other' equals the FlatMatrix.
    @Override
    public boolean equals(Object other) {
        if (this == other) {
             return true;
        }
        if (other instanceof Matrix) {
            Matrix otherMat = (Matrix) other;
            if (this.rows != otherMat.getNumRows() || this.cols != otherMat.getNumCols()) {
                 return false;
            }
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                     // Using Double.compare for safe floating-point comparison
                    if (Double.compare(this.get(i, j), otherMat.get(i, j)) != 0) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "FlatMatrix " + this.rows + " X " + this.cols;
    }
}
