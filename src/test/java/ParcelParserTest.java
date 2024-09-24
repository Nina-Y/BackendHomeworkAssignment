import com.example.model.Parcel;
import com.example.service.ParcelParser;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ParcelParserTest {

    @Test
    void testValidInputParsing() {
        Map<String, List<?>> parsedData = ParcelParser.parse("src/test/resources/test_input.txt");
        List<Parcel> validParcels = (List<Parcel>) parsedData.get("valid");

        assertNotNull(validParcels);
        assertFalse(validParcels.isEmpty());
        assertEquals(20, validParcels.size());
    }

    @Test
    void testInvalidInputParsing() {
        Map<String, List<?>> parsedData = ParcelParser.parse("src/test/resources/test_input.txt");
        List<String> invalidParcels = (List<String>) parsedData.get("invalid");

        assertNotNull(invalidParcels);
        assertFalse(invalidParcels.isEmpty());
        assertEquals(6, invalidParcels.size());
    }
}