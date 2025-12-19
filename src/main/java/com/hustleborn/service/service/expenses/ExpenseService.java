package com.hustleborn.service.service.expenses;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustleborn.service.model.expenses.Expense;
import com.hustleborn.service.repository.expenses.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense createExpense(Expense expense) {
        if (expense.getAmount() == null || expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Expense expense) {
        expense.setUpdatedAt(LocalDateTime.now());
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getExpensesByAccountId(Long accountId) {
        return expenseRepository.findByAccountId(accountId);
    }
}