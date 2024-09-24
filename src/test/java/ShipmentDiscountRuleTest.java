import com.example.MonthlyDiscountTracker;
import com.example.model.Parcel;
import com.example.rule.ShipmentDiscountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentDiscountRuleTest {

    private List<Parcel> validParcels;
    private Map<YearMonth, MonthlyDiscountTracker> discountTrackers;
    private Map<YearMonth, Integer> lpLCount;

    @BeforeEach
    void setUp() {
        validParcels = new ArrayList<>();
        discountTrackers = new HashMap<>();
        lpLCount = new HashMap<>();
    }

    @Test
    void testSmallParcelDiscountApplied() {
        Parcel parcel = new Parcel(LocalDate.of(2021, 12, 1), "S", "MR");
        validParcels.add(parcel);

        ShipmentDiscountRule.processParcel(parcel, discountTrackers, lpLCount);

        MonthlyDiscountTracker tracker = discountTrackers.get(YearMonth.of(2021, 12));
        assertNotNull(tracker);

        BigDecimal accumulatedDiscount = tracker.getAccumulatedDiscount();
        assertEquals(new BigDecimal("0.50"), accumulatedDiscount);
    }

    @Test
    void testLargeParcelLPDiscountApplied() {
        Parcel parcel1 = new Parcel(LocalDate.of(2021, 12, 1), "L", "LP");
        Parcel parcel2 = new Parcel(LocalDate.of(2021, 12, 2), "L", "LP");
        Parcel parcel3 = new Parcel(LocalDate.of(2021, 12, 3), "L", "LP");

        validParcels.add(parcel1);
        validParcels.add(parcel2);
        validParcels.add(parcel3);

        ShipmentDiscountRule.processParcel(parcel1, discountTrackers, lpLCount);
        ShipmentDiscountRule.processParcel(parcel2, discountTrackers, lpLCount);

        ShipmentDiscountRule.processParcel(parcel3, discountTrackers, lpLCount);

        MonthlyDiscountTracker tracker = discountTrackers.get(YearMonth.of(2021, 12));
        assertNotNull(tracker);

        BigDecimal accumulatedDiscount = tracker.getAccumulatedDiscount();
        assertEquals(new BigDecimal("6.90"), accumulatedDiscount);  // Assuming full discount on large parcel for LP
    }

    @Test
    void testMonthlyDiscountLimitApplied() {
        Parcel parcel1 = new Parcel(LocalDate.of(2021, 12, 1), "S", "MR");
        Parcel parcel2 = new Parcel(LocalDate.of(2021, 12, 2), "S", "MR");
        Parcel parcel3 = new Parcel(LocalDate.of(2021, 12, 3), "L", "LP");

        validParcels.add(parcel1);
        validParcels.add(parcel2);
        validParcels.add(parcel3);

        ShipmentDiscountRule.processParcel(parcel1, discountTrackers, lpLCount);
        ShipmentDiscountRule.processParcel(parcel2, discountTrackers, lpLCount);

        ShipmentDiscountRule.processParcel(parcel3, discountTrackers, lpLCount);

        MonthlyDiscountTracker tracker = discountTrackers.get(YearMonth.of(2021, 12));
        assertNotNull(tracker);

        BigDecimal accumulatedDiscount = tracker.getAccumulatedDiscount();

        assertTrue(accumulatedDiscount.compareTo(new BigDecimal("10.00")) <= 0,
                "Accumulated discount should not exceed 10.00");
    }

    @Test
    void testNoDiscountApplied() {
        Parcel parcel = new Parcel(LocalDate.of(2021, 12, 1), "L", "MR");
        validParcels.add(parcel);

        ShipmentDiscountRule.processParcel(parcel, discountTrackers, lpLCount);

        MonthlyDiscountTracker tracker = discountTrackers.get(YearMonth.of(2021, 12));
        assertNotNull(tracker);

        BigDecimal accumulatedDiscount = tracker.getAccumulatedDiscount();
        assertEquals(BigDecimal.ZERO, accumulatedDiscount);
    }

    @Test
    void testInvalidParcelIgnored() {
        Parcel parcel = new Parcel(LocalDate.of(2021, 12, 1), "INVALID_SIZE", "INVALID_COMPANY");

        assertDoesNotThrow(() -> ShipmentDiscountRule.processParcel(parcel, discountTrackers, lpLCount));

        assertNull(discountTrackers.get(YearMonth.of(2021, 12)));
    }
}
