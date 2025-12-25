package com.hustleborn.service.service.expenses;

import java.util.List;
import com.hustleborn.service.model.expenses.Expense;

public interface IExpenseService {
    List<Expense> getAllExpenses();
    Expense getExpenseById(Long id);
    Expense createExpense(Expense expense);
    Expense updateExpense(Expense expense);
    void deleteExpense(Long id);
    List<Expense> getExpensesByAccountId(Long accountId);
}