package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.rule.ShipmentDiscountRule.processLine;

public class ShipmentDiscountCalculator {

    public static void main(String[] args) {

        try {
            List<String> lines;

            if (args.length > 0) {
                lines = Files.readAllLines(Paths.get(args[0]));
            } else {
                lines = Files.readAllLines(Paths.get("src/main/resources/input.txt"));
            }

            Map<YearMonth, MonthlyDiscountTracker> discountTrackers = new HashMap<>();
            Map<YearMonth, Integer> lpLCount = new HashMap<>();

            for (String line : lines) {
                processLine(line, discountTrackers, lpLCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

