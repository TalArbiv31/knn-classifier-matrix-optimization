public class DemoHRClassifier {

    public static void main(String[] args) {
        System.out.println("--- IDF HR Data Analytics: k-NN Dropout Predictor ---");

        // 1. Load Mock HR Data (Historical Data)
        // Features: [Cognitive Score (Dapar), Medical Profile, Distance from Base]
        double[][] trainingFeatures = {
            {90, 97, 20},
            {40, 64, 150},
            {80, 82, 45},
            {50, 72, 200},
            {70, 97, 10}
        };
        
        // Labels: false = Completed Service, true = Dropped Out
        boolean[] trainingClasses = {false, true, false, true, false};

        System.out.println("[*] Loading historical data into FlatMatrix for cache efficiency...");
        Matrix X_train = new FlatMatrix(trainingFeatures);

        // 2. Define a new recruit (The instance we want to predict)
        // Recruit with Dapar 45, Profile 64, and lives 180km away
        double[][] recruitData = {{45, 64, 180}};
        Matrix X_recruit = new FlatMatrix(recruitData);

        // 3. Execute the Machine Learning Pipeline using your testClasses method
        int k = 3;
        System.out.println("[*] Running k-NN pipeline (Normalization, Distance Calculation, Sorting)...");
        
        // This single call uses your entire pipeline exactly as you wrote it
        boolean[] predictions = MatrixOperations.testClasses(X_train, trainingClasses, X_recruit, k);

        // 4. Output the business result
        System.out.println("\n--- Prediction Result ---");
        System.out.println("Based on the " + k + " most mathematically similar historical recruits:");
        
        if (predictions[0]) {
            System.out.println("HIGH RISK: The model predicts this recruit is likely to drop out.");
            System.out.println("Action: Recommend secondary placement (מיון שניוני).");
        } else {
            System.out.println("✅ LOW RISK: The model predicts successful service completion.");
        }
    }
}
