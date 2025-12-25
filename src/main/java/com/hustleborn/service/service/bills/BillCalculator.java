package com.hustleborn.service.service.bills;

import org.springframework.stereotype.Component;

import com.hustleborn.service.model.billitems.BillItems;
import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.strategy.discounts.DiscountStrategyFactory;
import com.hustleborn.service.strategy.discounts.IDiscountStrategy;

@Component
public class BillCalculator {

    public void calculateBillAmounts(Bills bill) {
        double total = 0.0;
        if (bill.getBillItems() != null) {
            for (BillItems item : bill.getBillItems()) {
                double itemTotal = item.getUnitPrice() * item.getQuantity();
                item.setTotalPrice(itemTotal);
                total += itemTotal;
            }
        }
        bill.setTotalAmount(total);

        double discountAmount = 0.0;
        if (bill.getDiscountType() != null && bill.getDiscountValue() != null) {
            IDiscountStrategy strategy = DiscountStrategyFactory.getStrategy(bill.getDiscountType());
            discountAmount = strategy.applyDiscount(total, bill.getDiscountValue());
        }
        bill.setDiscountAmount(discountAmount);
        bill.setFinalAmount(total - discountAmount);
    }
}