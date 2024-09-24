import com.example.ParcelProcessingRunner;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

class EndToEndTest {

    @Test
    void testEndToEndProcessing() throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        ParcelProcessingRunner.main(new String[]{"src/test/resources/test_input.txt"});

        String expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/test_output.txt")));
        String actualOutput = outputStream.toString();

        expectedOutput = expectedOutput.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
        actualOutput = actualOutput.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");

        assertEquals(expectedOutput.trim(), actualOutput.trim());
    }
}