public interface Matrix {
    // Assumes that i and j are valid indices of the matrix
    // rows and columns, respectively.
    double get(int i, int j);

    // Assumes that i and j are valid indices of the matrix
    // rows and columns, respectively.
    void set(int i, int j, double value);

    // Returns the number of rows of the matrix
    int getNumRows();

    // Returns the number of columns of the matrix
    int getNumCols();

    // Returns true if the matrix is square
    Boolean isSquare();

    // Creates a deep copy of the matrix.
    // (a) the new matrix has the same numbers of
    // rows and columns as the creating matrix.
    // (b) The values in the corresponding cells are
    // the same.
    // (c) Changing a value in one matrix does not change the
    // other.
    Matrix copy();

    // Creates a new matrix, which is the transpose of the
    // creating matrix.
    Matrix transpose();

}
