package com.example;

import java.math.BigDecimal;

public class MonthlyDiscountTracker {
    private BigDecimal accumulatedDiscount = BigDecimal.ZERO;

    public void addDiscount(BigDecimal discount) {
        accumulatedDiscount = accumulatedDiscount.add(discount);
    }

    public BigDecimal getAccumulatedDiscount() {
        return accumulatedDiscount;
    }
}