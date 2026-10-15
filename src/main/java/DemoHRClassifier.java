import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DemoHRClassifier {

    public static void main(String[] args) {
        System.out.println("--- IDF HR Data Analytics: k-NN Dropout Predictor ---");

        // 1. Load historical training data
        System.out.println("[*] Loading historical training data...");
        DataContainer trainData = loadDataFromCsv("hr_train_data.csv");
        Matrix X_train = new FlatMatrix(trainData.features);
        boolean[] y_train = trainData.labels;

        // 2. Load test data (new recruits / completely unseen data)
        System.out.println("[*] Loading new recruits test data...");
        DataContainer testData = loadDataFromCsv("hr_test_data.csv");
        Matrix X_test = new FlatMatrix(testData.features);
        boolean[] y_test_actual = testData.labels; // Ground truth, hidden from the algorithm

        // 3. Execute the Machine Learning Pipeline
        int k = 5;
        System.out.println("[*] Running k-NN pipeline (k=" + k + ")...");
        boolean[] predictions = MatrixOperations.testClasses(X_train, y_train, X_test, k);
        // 4. Evaluate the model performance
        System.out.println("[*] Evaluating model performance...");
        double accuracy = ModelEvaluator.measureAccuracy(predictions, y_test_actual);

        System.out.println("\n--- Evaluation Results ---");
        System.out.printf("Model Accuracy on completely unseen data: %.2f%%\n", (accuracy * 100));
    }

    /**
     * Helper method to read a CSV file and convert it into data arrays.
     */
    private static DataContainer loadDataFromCsv(String filePath) {
        List<double[]> featuresList = new ArrayList<>();
        List<Boolean> labelsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { 
                    isFirstLine = false; 
                    continue; 
                } // Skip header row
                
                String[] values = line.split(",");
                double dapar = Double.parseDouble(values[0]);
                double profile = Double.parseDouble(values[1]);
                double distance = Double.parseDouble(values[2]);
                boolean droppedOut = values[3].equals("1");
                
                featuresList.add(new double[]{dapar, profile, distance});
                labelsList.add(droppedOut);
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filePath);
            e.printStackTrace();
        }

        double[][] features = featuresList.toArray(new double[0][]);
        boolean[] labels = new boolean[labelsList.size()];
        for (int i = 0; i < labelsList.size(); i++) {
            labels[i] = labelsList.get(i);
        }

        return new DataContainer(features, labels);
    }

    /**
     * Internal helper class (Data Transfer Object) to package the parsed data arrays.
     */
    private static class DataContainer {
        double[][] features;
        boolean[] labels;

        DataContainer(double[][] features, boolean[] labels) {
            this.features = features;
            this.labels = labels;
        }
    }
}
