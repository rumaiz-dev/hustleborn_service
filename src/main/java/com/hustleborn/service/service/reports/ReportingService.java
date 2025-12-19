package com.hustleborn.service.service.reports;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.bills.BillItems;
import com.hustleborn.service.model.bills.Bills;
import com.hustleborn.service.model.bills.BillStatus;
import com.hustleborn.service.model.expenses.Expense;
import com.hustleborn.service.model.inventorytransactions.InventoryTransactions;
import com.hustleborn.service.model.inventorytransactions.TransactionType;
import com.hustleborn.service.model.reports.ProfitReport;
import com.hustleborn.service.repository.bills.BillRepository;
import com.hustleborn.service.repository.expenses.ExpenseRepository;
import com.hustleborn.service.repository.inventorytransactions.InventoryTransactionsRepository;

@Service
public class ReportingService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private InventoryTransactionsRepository inventoryTransactionsRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public ProfitReport getProfitReport() {
        List<Bills> allBills = billRepository.findAll();

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

        List<InventoryTransactions> damageTransactions = inventoryTransactionsRepository.findByTransactionType(TransactionType.Damage);
        double damagedLoss = 0.0;
        for (InventoryTransactions trans : damageTransactions) {
            Double purchasingPrice = trans.getVariant().getProduct().getPurchasingPrice();
            damagedLoss += -trans.getQuantity() * (purchasingPrice != null ? purchasingPrice : 0.0); 
        }

        List<Expense> expenses = expenseRepository.findAll();
        double totalExpenses = 0.0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount() != null ? expense.getAmount() : 0.0;
        }

        double netProfit = grossProfit - totalDiscounts - damagedLoss - totalExpenses;

        return new ProfitReport(salesVolume, grossProfit, netProfit, damagedLoss, totalExpenses);
    }
}