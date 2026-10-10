
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatrixUtils {

    /**
     * Prints a Matrix to the console.
     * @param mat The Matrix to print.
     */
    public static void printMatrix(Matrix mat) {
        int rows = mat.getNumRows();
        int cols = mat.getNumCols();
        String s = "";
        s += "[";

        for (int i = 0; i < rows; i++) {

            if (i > 0) {
                s += " ";
            }
            s += "[";

            for (int j = 0; j < cols; j++) {
                s += mat.get(i, j);
                if (j < cols - 1) {
                    s += " ";
                }
            }

            s += "]";

            if (i < rows - 1) {
                s += "\n";
            }
        }

        s += "]";
        System.out.println(s);
    }


    /**
     * Reads a CSV file and returns a Matrix containing the feature data.
     * It assumes the first row is a header and skips it.
     * It assumes the last column is the result/label and excludes it.
     *
     * @param filePath The path to the CSV file.
     * @return A Matrix containing the feature data.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalArgumentException If the file contains no data rows or no feature columns.
     */
    public static double[][] readFeaturesFromCSV(String filePath) throws IOException {
        List<double[]> featureRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header row.
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] values = line.split(",");
                // Ensure there is at least one feature column and a result column
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

        // The FlatMatrix constructor can take a 2D double array directly.
        return featureRows.toArray(new double[0][]);
    }

    /**
     * Reads a CSV file and returns a boolean array of labels from the last column.
     * It assumes the first row is a header and skips it.
     * It converts '1' to true and '0' to false.
     *
     * @param filePath The path to the CSV file.
     * @return A boolean array of labels.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalArgumentException If the file is empty or a row has no columns.
     */
    public static boolean[] readLabelsFromCSV(String filePath) throws IOException {
        List<Boolean> labels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",");
                if (values.length > 0) {
                    String labelStr = values[values.length - 1].trim();
                    int labelValue = Integer.parseInt(labelStr);
                    labels.add(labelValue == 1);
                }
            }
        }

        boolean[] result = new boolean[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            result[i] = labels.get(i);
        }
        return result;
    }

    public static void roundMatrix(Matrix mat) {
        int rows = mat.getNumRows();
        int cols = mat.getNumCols();

        for(int i = 0; i < rows;i++){
            for(int j = 0; j < cols;j++){
                mat.set(i,j,roundToFourDecimals(mat.get(i,j)));
            }
        }
    }

    private static double roundToFourDecimals(double value) {
        double scale = 10000;
        double shiftedValue = value * scale;
        long roundedLong = Math.round(shiftedValue);
        return (double) roundedLong / scale;
    }


}
