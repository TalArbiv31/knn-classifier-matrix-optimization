import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DemoHRClassifier {

    public static void main(String[] args) {
        System.out.println("--- IDF HR Data Analytics: k-NN Dropout Predictor ---");
        System.out.println("[*] Reading historical data from mock_hr_data.csv...");

        List<double[]> featuresList = new ArrayList<>();
        List<Boolean> labelsList = new ArrayList<>();

        // 1. Read the CSV file dynamically
        // Make sure the file "mock_hr_data.csv" is in your project root directory
        try (BufferedReader br = new BufferedReader(new FileReader("mock_hr_data.csv"))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isFirstLine) { 
                    isFirstLine = false; 
                    continue; //skips to the next iteration.
                }
                
                // Split the row by commas
                String[] values = line.split(",");
                
                // Parse the Strings into numbers
                double dapar = Double.parseDouble(values[0]);
                double profile = Double.parseDouble(values[1]);
                double distance = Double.parseDouble(values[2]);
                boolean droppedOut = values[3].equals("1"); // true if "1", false if "0"
                
                featuresList.add(new double[]{dapar, profile, distance});
                labelsList.add(droppedOut);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file. Make sure it exists in the correct folder.");
            e.printStackTrace();
            return;
        }

        // 2. Convert dynamic Lists to static arrays for our Matrix Operations
        double[][] trainingFeatures = featuresList.toArray(new double[0][]);
        boolean[] trainingClasses = new boolean[labelsList.size()];
        for (int i = 0; i < labelsList.size(); i++) {
            trainingClasses[i] = labelsList.get(i);
        }

        System.out.println("[*] Successfully loaded " + trainingFeatures.length + " historical records.");
        System.out.println("[*] Loading data into FlatMatrix for cache efficiency...");
        Matrix X_train = new FlatMatrix(trainingFeatures);

        // 3. Define a new recruit (The instance we want to predict)
        // Recruit with Dapar 45, Profile 64, and lives 180km away
        double[][] recruitData = {{45, 64, 180}};
        Matrix X_recruit = new FlatMatrix(recruitData);

        // 4. Execute the Machine Learning Pipeline
        int k = 3;
        System.out.println("[*] Running k-NN pipeline (Normalization, Distance Calculation, Sorting)...");
        boolean[] predictions = MatrixOperations.testClasses(X_train, trainingClasses, X_recruit, k);

        // 5. Output the business result
        System.out.println("\n--- Prediction Result ---");
        System.out.println("Based on the " + k + " most mathematically similar historical recruits:");
        
        if (predictions[0]) {
            System.out.println("HIGH RISK: The model predicts this recruit is likely to drop out.");
            System.out.println("Action: Recommend secondary placement.");
        } else {
            System.out.println("LOW RISK: The model predicts successful service completion.");
        }
    }
}
