import java.util.Comparator;

/**
 * Comparator designed to sort indices based on their corresponding 
 * distance values within a specific row of a distance matrix.
 */
public class IndexComparator implements Comparator<Integer> {

    private final Matrix mat;
    private final int row;

    public IndexComparator(Matrix m, int row) {
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (row < 0 || row >= m.getNumRows()) {
            throw new IllegalArgumentException("Invalid row index: " + row);
        }
        this.mat = m;
        this.row = row;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("Index arguments cannot be null.");
        }
        if (o1 < 0 || o1 >= this.mat.getNumCols() || o2 < 0 || o2 >= this.mat.getNumCols()) {
            throw new IllegalArgumentException("Index arguments are out of matrix column bounds.");
        }
        
        double dist1 = this.mat.get(this.row, o1);
        double dist2 = this.mat.get(this.row, o2);
        
        return Double.compare(dist1, dist2);
    }
}
