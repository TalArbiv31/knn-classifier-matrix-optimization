import java.io.IOException;

public class DemoHRClassifier {

    public static void main(String[] args) {
        System.out.println("--- IDF HR Data Analytics: k-NN Dropout Predictor ---");

        // Updated paths to match the GitHub repository structure
        String trainFilePath = "resources/hr_train_data.csv";
        String testFilePath = "resources/hr_test_data.csv";

        try {
            // 1. Load historical training data using the existing MatrixUtils utility
            System.out.println("[*] Loading historical training data...");
            double[][] trainFeatures = MatrixUtils.readFeaturesFromCSV(trainFilePath);
            boolean[] y_train = MatrixUtils.readLabelsFromCSV(trainFilePath);
            Matrix X_train = new FlatMatrix(trainFeatures);

            // 2. Load test data (new recruits / completely unseen data)
            System.out.println("[*] Loading new recruits test data...");
            double[][] testFeatures = MatrixUtils.readFeaturesFromCSV(testFilePath);
            boolean[] y_test_actual = MatrixUtils.readLabelsFromCSV(testFilePath);
            Matrix X_test = new FlatMatrix(testFeatures);

            // 3. Execute the Machine Learning Pipeline
            int k = 5;
            System.out.println("[*] Running k-NN pipeline (k=" + k + ")...");
            boolean[] predictions = MatrixOperations.testClasses(X_train, y_train, X_test, k);
            
            // 4. Evaluate the model performance
            System.out.println("[*] Evaluating model performance...");
            double accuracy = ModelEvaluator.measureAccuracy(predictions, y_test_actual);

            System.out.println("\n--- Evaluation Results ---");
            System.out.printf("Model Accuracy on completely unseen data: %.2f%%\n", (accuracy * 100));

        } catch (IOException e) {
            System.err.println("[!] Critical Error: Could not find or read the data files.");
            System.err.println("Make sure you are running the program from the project root directory");
            System.err.println("and that the files exist in the 'resources/' folder.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("[!] Data Error: " + e.getMessage());
        }
    }
}
