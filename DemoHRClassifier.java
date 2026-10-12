import java.util.Arrays;

public class DemoHRClassifier {

    public static void main(String[] args) {
        System.out.println("--- IDF HR Data Analytics: k-NN Dropout Predictor ---");

        // 1. Load Mock HR Data (In reality, parsed from mock_hr_data.csv)
        // Features: [Cognitive Score (Dapar), Medical Profile, Distance from Base]
        double[][] trainingFeatures = {
            {90, 97, 20},
            {40, 64, 150},
            {80, 82, 45},
            {50, 72, 200},
            {70, 97, 10}
        };
        
        // Labels: 0 = Completed Service, 1 = Dropped Out
        int[] labels = {0, 1, 0, 1, 0};

        // 2. Initialize the highly-optimized FlatMatrix
        System.out.println("[*] Loading historical data into FlatMatrix for cache efficiency...");
        Matrix X_train = new FlatMatrix(trainingFeatures);

        // 3. Define a new recruit (The instance we want to predict)
        // E.g., A recruit with Dapar 45, Profile 64, and lives 180km away
        double[][] recruitData = {{45, 64, 180}};
        Matrix X_recruit = new FlatMatrix(recruitData);

        // 4. Normalize Data (Z-score) to prevent distance domination
        System.out.println("[*] Applying Z-score normalization across all features...");
        // כאן אתה קורא לפונקציית הנרמול שבנית ב-MatrixOperations
        // MatrixOperations.zScoreNormalize(X_train);
        // MatrixOperations.zScoreNormalize(X_recruit, X_train_mean, X_train_std);

        // 5. Calculate Squared Distances
        System.out.println("[*] Calculating vectorized squared distances...");
        // כאן אתה מחשב מרחקים באמצעות הפעולות הליניאריות שלך
        // double[] distances = MatrixOperations.calculateSquaredDistances(X_train, X_recruit);
        
        // Mock distances for the sake of the demo outline
        double[] distances = {15.4, 2.1, 12.8, 1.5, 18.2}; 

        // 6. Sort using your custom IndexComparator to keep track of the original labels
        System.out.println("[*] Sorting neighbors using custom IndexComparator...");
        Integer[] indices = {0, 1, 2, 3, 4};
        Arrays.sort(indices, new IndexComparator(distances));

        // 7. Majority Voting (k = 3)
        int k = 3;
        int dropouts = 0;
        int completions = 0;
        
        for (int i = 0; i < k; i++) {
            int originalIndex = indices[i];
            if (labels[originalIndex] == 1) {
                dropouts++;
            } else {
                completions++;
            }
        }

        // 8. Output the business result!
        System.out.println("\n--- Prediction Result ---");
        System.out.println("Based on the " + k + " most mathematically similar historical recruits:");
        if (dropouts > completions) {
            System.out.println("⚠️ HIGH RISK: The model predicts this recruit is likely to drop out.");
            System.out.println("Action: Recommend secondary placement (מיון שניוני).");
        } else {
            System.out.println("✅ LOW RISK: The model predicts successful service completion.");
        }
    }
}
