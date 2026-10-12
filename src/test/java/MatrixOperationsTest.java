import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {

    @Test
    public void testMatrixAddition() {
        // 1. Arrange - Set up test data
        double[][] data1 = {{1, 2}, {3, 4}};
        double[][] data2 = {{5, 6}, {7, 8}};
        
        Matrix m1 = new FlatMatrix(data1);
        Matrix m2 = new FlatMatrix(data2);

        // 2. Act - Execute the method under test
        Matrix result = MatrixOperations.add(m1, m2);

        // 3. Assert - Verify the result matches expectations (1+5=6, 2+6=8...)
        assertEquals(6.0, result.get(0, 0), "1 + 5 should be 6");
        assertEquals(8.0, result.get(0, 1), "2 + 6 should be 8");
        assertEquals(10.0, result.get(1, 0), "3 + 7 should be 10");
        assertEquals(12.0, result.get(1, 1), "4 + 8 should be 12");
    }

    @Test
    public void testAdditionMismatchedDimensions() {
        // Verify that an exception is thrown when dimensions do not match (edge case)
        Matrix m1 = new FlatMatrix(new double[][]{{1, 2}});
        Matrix m2 = new FlatMatrix(new double[][]{{1}, {2}});

        // Expect the method to throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            MatrixOperations.add(m1, m2);
        });
    }
}
