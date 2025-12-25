package com.hustleborn.service.strategy.discounts;

public class FixedDiscountStrategy implements IDiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount, double discountValue) {
        return discountValue;
    }
}