package com.hustleborn.service.service.reports;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.billitems.BillItems;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.model.expenses.Expense;
import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.reports.ProfitReport;

@Component
public class ProfitCalculator {

    public ProfitReport calculateProfitReport(List<Bills> allBills, List<InventoryTransactions> damageTransactions, List<Expense> expenses) {
        double salesVolume = 0.0;
        double cogs = 0.0;
        double totalDiscounts = 0.0;

        for (Bills bill : allBills) {
            if (bill.getStatus() == BillStatus.Paid) {
                salesVolume += bill.getFinalAmount() != null ? bill.getFinalAmount() : 0.0;
                totalDiscounts += bill.getDiscountAmount() != null ? bill.getDiscountAmount() : 0.0;
            }

            if (bill.getBillItems() != null) {
                for (BillItems item : bill.getBillItems()) {
                    int effectiveQty = item.getQuantity() - (item.getRefundedQuantity() != null ? item.getRefundedQuantity() : 0);
                    Double purchasingPrice = item.getVariant().getProduct().getPurchasingPrice();
                    if (effectiveQty > 0) {
                        cogs += effectiveQty * (purchasingPrice != null ? purchasingPrice : 0.0);
                    }
                }
            }
        }

        double grossProfit = salesVolume - cogs;

        double damagedLoss = 0.0;
        for (InventoryTransactions trans : damageTransactions) {
            Double purchasingPrice = trans.getVariant().getProduct().getPurchasingPrice();
            damagedLoss += -trans.getQuantity() * (purchasingPrice != null ? purchasingPrice : 0.0);
        }

        double totalExpenses = 0.0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount() != null ? expense.getAmount() : 0.0;
        }

        double netProfit = grossProfit - totalDiscounts - damagedLoss - totalExpenses;

        return new ProfitReport(salesVolume, grossProfit, netProfit, damagedLoss, totalExpenses);
    }
}