package com.SpringBootMVC.ExpensesTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBootMVC.ExpensesTracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    //List<Expense> findAll(int id);
}
