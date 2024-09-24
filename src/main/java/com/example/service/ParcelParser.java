package com.example.service;

import com.example.model.Company;
import com.example.model.Parcel;
import com.example.model.ParcelSize;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcelParser {

    public static Map<String, List<?>> parse(String fileName) {
        List<Parcel> validParcels = new ArrayList<>();
        List<String> invalidParcels = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));

            for (String line : lines) {
                parseLine(line, validParcels, invalidParcels);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("valid", validParcels);
        result.put("invalid", invalidParcels);

        return result;
    }

    private static void parseLine(String line, List<Parcel> validParcels, List<String> invalidParcels) {
        String[] parts = line.split("\\s+");

        if (parts.length != 3) {
            invalidParcels.add(line + " Ignored (parts.length != 3)");
            return;
        }

        try {
            ParcelSize size = ParcelSize.valueOf(parts[1]);
            Company company = Company.valueOf(parts[2]);
            LocalDate date = LocalDate.parse(parts[0]);

            Parcel parcel = new Parcel(date, size.toString(), company.toString());
            validParcels.add(parcel);
        } catch (IllegalArgumentException e) {
            invalidParcels.add(line + " Ignored (Invalid size or company)");
        } catch (DateTimeParseException e) {
            invalidParcels.add(line + " Ignored (Invalid date)");
        } catch (Exception e) {
            invalidParcels.add(line + " Ignored");
        }
    }
}