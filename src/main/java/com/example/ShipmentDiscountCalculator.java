package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentDiscountCalculator {

    private static final Map<String, Double> PRICES = new HashMap<>();
    private static final Map<String, Double> MIN_PRICES = new HashMap<>();

    static {
        try {
            loadPrices("src/main/resources/prices.txt", PRICES);
            loadPrices("src/main/resources/min_prices.txt", MIN_PRICES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Compile and run the program: mvn compile exec:java -Prun -Dexec.args="src/main/resources/input.txt"
    // Run tests: mvn test
    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.out.println("Usage: java com.example.ShipmentDiscountCalculator <input file>");
            return;
        }*/ // to run in command line

        try {
            List<String> lines = Files.readAllLines(Paths.get(args[0])); // to run in command line
            //List<String> lines = Files.readAllLines(Paths.get("src/main/resources/input.txt")); // to run in Intellij
            Map<YearMonth, MonthlyDiscountTracker> discountTrackers = new HashMap<>();
            Map<YearMonth, Integer> lpLCount = new HashMap<>();

            for (String line : lines) {
                processLine(line, discountTrackers, lpLCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadPrices(String filePath, Map<String, Double> map) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim();
                Double value = Double.valueOf(parts[1].trim());
                map.put(key, value);
            }
        }
    }

    private static void processLine(String line, Map<YearMonth, MonthlyDiscountTracker> discountTrackers, Map<YearMonth, Integer> lpLCount) {
        String[] parts = line.split("\\s+");
        if (parts.length != 3) {
            System.out.println(line + " Ignored");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(parts[0]);
            YearMonth yearMonth = YearMonth.from(date);
            String size = parts[1];
            String carrier = parts[2];

            if (!PRICES.containsKey(carrier + "_" + size)) {
                System.out.println(line + " Ignored");
                return;
            }

            double price = PRICES.get(carrier + "_" + size);
            double discount = 0.0;

            if (size.equals("S")) {
                price = MIN_PRICES.get("S");
                discount = PRICES.get(carrier + "_" + size) - price;
            } else if (size.equals("L") && carrier.equals("LP")) {
                lpLCount.putIfAbsent(yearMonth, 0);
                int count = lpLCount.get(yearMonth) + 1;
                lpLCount.put(yearMonth, count);

                if (count == 3) {
                    discount = price;
                    price = 0.0;
                }
            }

            discountTrackers.putIfAbsent(yearMonth, new MonthlyDiscountTracker());
            MonthlyDiscountTracker tracker = discountTrackers.get(yearMonth);

            if (tracker.getAccumulatedDiscount() + discount > 10.0) {
                discount = 10.0 - tracker.getAccumulatedDiscount();
                price = PRICES.get(carrier + "_" + size) - discount;
            }

            tracker.addDiscount(discount);
            System.out.printf("%s %s %s %.2f %s%n", parts[0], parts[1], parts[2], price, discount > 0 ? String.format("%.2f", discount) : "-");
        } catch (Exception e) {
            System.out.println(line + " Ignored");
        }
    }
}

