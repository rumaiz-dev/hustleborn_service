package com.hustleborn.service.strategy.discounts;

public interface IDiscountStrategy {
    double applyDiscount(double totalAmount, double discountValue);
}