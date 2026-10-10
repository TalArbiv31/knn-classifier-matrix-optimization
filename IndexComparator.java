
import java.util.Comparator;

public class IndexComparator implements Comparator<Integer> {

    double[] array;
    private Matrix mat;
    private int row;

    public IndexComparator(Matrix m, int row){
        // ---------------write your code BELOW this line only! ------------------
        if (m == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        if (row < 0 || row >= m.getNumRows()) {
            throw new IllegalArgumentException("Invalid row index");
        }
        this.mat = m;
        this.row = row;
        // ---------------write your code ABOVE this line only! ------------------
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        int ans = 0;
        // ---------------write your code BELOW this line only! ------------------
        double dist1 = mat.get(this.row, o1);
        double dist2 = mat.get(this.row, o2);
        if (dist1 < dist2) {
            ans = -1;
        }
        else if (dist1 > dist2) {
            ans = 1;
        }
        // ---------------write your code ABOVE this line only! ------------------
        return ans;
    }
}
