package com.hustleborn.service.service.reports;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.bills.Bills;
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

    @Autowired
    private ProfitCalculator profitCalculator;

    public ProfitReport getProfitReport() {
        List<Bills> allBills = billRepository.findAll();
        List<InventoryTransactions> damageTransactions = inventoryTransactionsRepository.findByTransactionType(TransactionType.Damage);
        List<Expense> expenses = expenseRepository.findAll();

        return profitCalculator.calculateProfitReport(allBills, damageTransactions, expenses);
    }
}