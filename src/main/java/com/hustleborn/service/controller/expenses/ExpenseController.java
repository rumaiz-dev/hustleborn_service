package com.hustleborn.service.controller.expenses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.expenses.Expense;
import com.hustleborn.service.service.expenses.ExpenseService;
import com.hustleborn.service.utils.responses.ApiResponse;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        Map<String, Object> data = new HashMap<>();
        data.put("expenses", expenses);
        ApiResponse response = new ApiResponse(true, "Expenses retrieved successfully", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("expense", expense);
            ApiResponse response = new ApiResponse(true, "Expense retrieved successfully", data);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(false, "Expense not found", null);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createExpense(@RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(expense);
        Map<String, Object> data = new HashMap<>();
        data.put("expense", createdExpense);
        ApiResponse response = new ApiResponse(true, "Expense created successfully", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        expense.setId(id);
        Expense updatedExpense = expenseService.updateExpense(expense);
        Map<String, Object> data = new HashMap<>();
        data.put("expense", updatedExpense);
        ApiResponse response = new ApiResponse(true, "Expense updated successfully", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        ApiResponse response = new ApiResponse(true, "Expense deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}