package com.hustleborn.service.strategy.discounts;

import com.hustleborn.service.model.discounts.DiscountType;

public class DiscountStrategyFactory {
    public static IDiscountStrategy getStrategy(DiscountType discountType) {
        switch (discountType) {
            case Percentage:
                return new PercentageDiscountStrategy();
            case Fixed:
                return new FixedDiscountStrategy();
            default:
                throw new IllegalArgumentException("Unknown discount type: " + discountType);
        }
    }
}