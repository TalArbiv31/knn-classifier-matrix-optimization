public class ModelEvaluator {

    /**
     * Compares the predicted labels with the actual labels to calculate the accuracy percentage.
     * 
     * @param predictions boolean array of the model's predictions
     * @param actual boolean array of the real ground truth labels
     * @return a double between 0.0 and 1.0 representing the accuracy
     */
    public static double measureAccuracy(boolean[] predictions, boolean[] actual) {
        if (predictions == null || actual == null) {
            throw new IllegalArgumentException("Arrays cannot be null");
        }
        if (predictions.length != actual.length) {
            throw new IllegalArgumentException("Predictions and actuals must have the same length");
        }
        if (predictions.length == 0) {
            return 0.0;
        }

        int correctCount = 0;
        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] == actual[i]) {
                correctCount++;
            }
        }
        
        return (double) correctCount / predictions.length;
    }
}
