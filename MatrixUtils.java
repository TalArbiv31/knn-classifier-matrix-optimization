import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing helper methods for matrix input/output, formatting, and data extraction.
 */
public class MatrixUtils {

    /**
     * Prints a Matrix to the console in a readable 2D array format.
     * Uses StringBuilder for efficient string concatenation.
     * 
     * @param mat The Matrix to print.
     */
    public static void printMatrix(Matrix mat) {
        if (mat == null) {
            System.out.println("null");
            return;
        }

        int rows = mat.getNumRows();
        int cols = mat.getNumCols();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < rows; i++) {
            sb.append("  [");
            for (int j = 0; j < cols; j++) {
                sb.append(mat.get(i, j));
                if (j < cols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (i < rows - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    /**
     * Reads a CSV file and returns a 2D array containing the feature data.
     * It assumes the first row is a header and skips it.
     * It assumes the last column is the class label and excludes it.
     *
     * @param filePath The path to the CSV file.
     * @return A 2D double array containing the feature data.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalArgumentException If the file contains no data rows.
     */
    public static double[][] readFeaturesFromCSV(String filePath) throws IOException {
        List<double[]> featureRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header row
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] values = line.split(",");
                // Ensure there is at least one feature column and one label column
                if (values.length > 1) {
                    double[] row = new double[values.length - 1];
                    for (int i = 0; i < values.length - 1; i++) {
                        row[i] = Double.parseDouble(values[i].trim());
                    }
                    featureRows.add(row);
                }
            }
        }

        if (featureRows.isEmpty()) {
            throw new IllegalArgumentException("CSV file contains no data rows or is empty.");
        }

        return featureRows.toArray(new double[0][]);
    }

    /**
     * Reads a CSV file and returns a boolean array of labels from the last column.
     * It assumes the first row is a header and skips it.
     * It converts '1' to true and '0' (or any other value) to false.
     *
     * @param filePath The path to the CSV file.
     * @return A boolean array representing the ground truth labels.
     * @throws IOException If an I/O error occurs.
     */
    public static boolean[] readLabelsFromCSV(String filePath) throws IOException {
        List<Boolean> labels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header row
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",");
                if (values.length > 0) {
                    String labelStr = values[values.length - 1].trim();
                    // Assuming binary classification where 1 is true, others are false
                    int labelValue = Integer.parseInt(labelStr);
                    labels.add(labelValue == 1);
                }
            }
        }

        // Convert List<Boolean> to primitive boolean[]
        boolean[] result = new boolean[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            result[i] = labels.get(i);
        }
        return result;
    }

    /**
     * Rounds all elements in the given matrix to four decimal places.
     * Modifies the matrix in-place.
     * 
     * @param mat The matrix to round.
     */
    public static void roundMatrix(Matrix mat) {
        if (mat == null) return;
        
        int rows = mat.getNumRows();
        int cols = mat.getNumCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat.set(i, j, roundToFourDecimals(mat.get(i, j)));
            }
        }
    }

    /**
     * Helper method to round a single double value to four decimal places.
     */
    private static double roundToFourDecimals(double value) {
        double scale = 10000.0;
        return Math.round(value * scale) / scale;
    }
}
