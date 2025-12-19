package com.hustleborn.service.repository.expenses;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hustleborn.service.model.expenses.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByAccountId(Long accountId);
}