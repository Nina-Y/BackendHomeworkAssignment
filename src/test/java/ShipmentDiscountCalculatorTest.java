import com.example.ShipmentDiscountCalculator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShipmentDiscountCalculatorTest {

    @Test
    public void testExample() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShipmentDiscountCalculator.main(new String[] {"src/test/resources/test_input.txt"});

        String expectedOutput = Files.readString(Paths.get("src/test/resources/test_output.txt"));
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }
}

