package com.example;

public class MonthlyDiscountTracker {
    private double accumulatedDiscount;

    public void addDiscount(double discount) {
        accumulatedDiscount += discount;
    }

    public double getAccumulatedDiscount() {
        return accumulatedDiscount;
    }
}

