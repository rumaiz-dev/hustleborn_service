package com.hustleborn.service.strategy.discounts;

public class FixedDiscountStrategy implements IDiscountStrategy {
    
    public double applyDiscount(double totalAmount, double discountValue) {
        return discountValue;
    }
}