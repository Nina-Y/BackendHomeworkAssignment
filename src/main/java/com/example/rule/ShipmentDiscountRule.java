package com.example.rule;

import com.example.MonthlyDiscountTracker;
import com.example.model.Parcel;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentDiscountRule {

    private static final Map<String, BigDecimal> PRICES = new HashMap<>();
    private static final Map<String, BigDecimal> MIN_PRICES = new HashMap<>();

    static {
        try {
            loadPrices("src/main/resources/prices.txt", PRICES);
            loadPrices("src/main/resources/min_prices.txt", MIN_PRICES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processValidParcels(List<Parcel> validParcels) {
        Map<YearMonth, MonthlyDiscountTracker> discountTrackers = new HashMap<>();
        Map<YearMonth, Integer> lpLCount = new HashMap<>();

        for (Parcel validParcel : validParcels) {
            processParcel(validParcel, discountTrackers, lpLCount);
        }
    }

    public static void processParcel(Parcel parcel, Map<YearMonth, MonthlyDiscountTracker> discountTrackers, Map<YearMonth, Integer> lpLCount) {
        try {
            LocalDate date = parcel.date();
            YearMonth yearMonth = YearMonth.from(date);
            String size = parcel.size();
            String company = parcel.company();

            BigDecimal price = validateAndGetPrice(parcel);
            if (price == null) {
                return;
            }

            BigDecimal discount = applyDiscounts(parcel, yearMonth, discountTrackers, lpLCount, price);

            price = price.subtract(discount);

            // Track the applied discount
            discountTrackers.putIfAbsent(yearMonth, new MonthlyDiscountTracker());
            MonthlyDiscountTracker tracker = discountTrackers.get(yearMonth);
            tracker.addDiscount(discount);

            System.out.printf("%s %s %s %.2f %s%n", date, size, company, price, discount.compareTo(BigDecimal.ZERO) > 0 ? String.format("%.2f", discount) : "-");

        } catch (Exception e) {
            System.out.println(parcel + " Invalid");
        }
    }

    private static BigDecimal validateAndGetPrice(Parcel parcel) {
        String size = parcel.size();
        String company = parcel.company();

        if (!PRICES.containsKey(company + "_" + size)) {
            System.out.println(parcel + " Ignored");
            return null;
        }

        return PRICES.get(company + "_" + size);
    }

    private static BigDecimal applyDiscounts(Parcel parcel, YearMonth yearMonth, Map<YearMonth, MonthlyDiscountTracker> discountTrackers, Map<YearMonth, Integer> lpLCount, BigDecimal price) {
        String size = parcel.size();
        String company = parcel.company();
        BigDecimal discount = BigDecimal.ZERO;

        if (size.equals("S")) {
            discount = applySmallSizeDiscount(company, size);
        }
        else if (size.equals("L") && company.equals("LP")) {
            discount = applyLargeSizeLPDiscount(yearMonth, lpLCount, price);
        }

        MonthlyDiscountTracker tracker = discountTrackers.getOrDefault(yearMonth, new MonthlyDiscountTracker());
        discount = applyDiscountLimit(tracker, discount);

        return discount;
    }

    private static BigDecimal applySmallSizeDiscount(String company, String size) {
        BigDecimal minPrice = MIN_PRICES.get(size);
        BigDecimal originalPrice = PRICES.get(company + "_" + size);
        return originalPrice.subtract(minPrice);
    }

    private static BigDecimal applyLargeSizeLPDiscount(YearMonth yearMonth, Map<YearMonth, Integer> lpLCount, BigDecimal price) {
        lpLCount.putIfAbsent(yearMonth, 0);
        int count = lpLCount.get(yearMonth) + 1;
        lpLCount.put(yearMonth, count);

        if (count == 3) {
            return price;
        }
        return BigDecimal.ZERO;
    }

    private static BigDecimal applyDiscountLimit(MonthlyDiscountTracker tracker, BigDecimal discount) {
        BigDecimal maxDiscount = new BigDecimal("10.0");
        BigDecimal accumulatedDiscount = tracker.getAccumulatedDiscount();

        if (accumulatedDiscount.add(discount).compareTo(maxDiscount) > 0) {
            discount = maxDiscount.subtract(accumulatedDiscount);
        }
        return discount;
    }

    private static void loadPrices(String filePath, Map<String, BigDecimal> priceMap) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            String[] parts = line.split("=");
            priceMap.put(parts[0], new BigDecimal(parts[1]));
        }
    }
}