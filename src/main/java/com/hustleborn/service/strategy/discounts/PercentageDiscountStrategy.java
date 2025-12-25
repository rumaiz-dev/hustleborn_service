package com.hustleborn.service.strategy.discounts;

public class PercentageDiscountStrategy implements IDiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount, double discountValue) {
        return totalAmount * (discountValue / 100.0);
    }
}