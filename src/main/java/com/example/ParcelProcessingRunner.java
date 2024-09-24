package com.example;

import com.example.model.Parcel;
import com.example.rule.ShipmentDiscountRule;
import com.example.service.ParcelParser;
import java.util.List;
import java.util.Map;

public class ParcelProcessingRunner {

    public static void main(String[] args) {

        String fileName = (args.length > 0) ? args[0] : "src/main/resources/input.txt";

        Map<String, List<?>> parsedData = ParcelParser.parse(fileName);

        List<Parcel> validParcels = (List<Parcel>) parsedData.get("valid");
        List<String> invalidParcels = (List<String>) parsedData.get("invalid");

        validParcels.sort((p1, p2) -> p1.date().compareTo(p2.date()));

        ShipmentDiscountRule.processValidParcels(validParcels);

        invalidParcels.forEach(System.out::println);
    }
}