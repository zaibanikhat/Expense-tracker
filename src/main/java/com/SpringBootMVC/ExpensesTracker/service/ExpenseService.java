package com.SpringBootMVC.ExpensesTracker.service;

import java.util.List;
import java.util.Map;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;

public interface ExpenseService {

    Expense findExpenseById(int id);

    void save(ExpenseDTO expenseDTO);

    void update(ExpenseDTO expenseDTO);

    List<Expense> findAll();

    void deleteExpenseById(int id);

    List<Expense> findFilterResult(FilterDTO filter);

    //overview methods

    double getTotalAmount();

    long getTotalExpensesCount();

    double getCurrentMonthTotal();

    Map<String, Double> getCategoryWiseTotal();
}
