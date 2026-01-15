package com.SpringBootMVC.ExpensesTracker.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.repository.ExpenseRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final EntityManager entityManager;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              CategoryService categoryService,
                              EntityManager entityManager) {
        this.expenseRepository = expenseRepository;
        this.categoryService = categoryService;
        this.entityManager = entityManager;
    }

    @Override
    public Expense findExpenseById(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(ExpenseDTO expenseDTO) {

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDateTime(expenseDTO.getDateTime());
        expense.setDescription(expenseDTO.getDescription());

        Category category = categoryService.findCategoryByName(expenseDTO.getCategory());
        expense.setCategory(category);

        expenseRepository.save(expense);
    }

    @Override
    public void update(ExpenseDTO expenseDTO) {

        Expense existingExpense =
                expenseRepository.findById(expenseDTO.getExpenseId()).orElse(null);

        if (existingExpense == null) {
            return;
        }

        existingExpense.setAmount(expenseDTO.getAmount());
        existingExpense.setDateTime(expenseDTO.getDateTime());
        existingExpense.setDescription(expenseDTO.getDescription());

        Category category = categoryService.findCategoryByName(expenseDTO.getCategory());
        if (category == null) {
        throw new RuntimeException("Category not found: " + expenseDTO.getCategory());
    }
        existingExpense.setCategory(category);

        expenseRepository.save(existingExpense);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public void deleteExpenseById(int id) {
        expenseRepository.deleteById(id);
    }

   @Override
    public List<Expense> findFilterResult(FilterDTO filter) {

        StringBuilder query =
            new StringBuilder("select e from Expense e where e.dateTime IS NOT NULL");

        // category
        if (!"all".equals(filter.getCategory())) {
            int categoryId =
                categoryService.findCategoryByName(filter.getCategory()).getId();
            query.append(" AND e.category.id = ").append(categoryId);
        }

        // amount
        query.append(" AND e.amount between ")
            .append(filter.getFrom())
            .append(" AND ")
            .append(filter.getTo());

        // year
        if (!"all".equals(filter.getYear())) {
            query.append(" AND substring(e.dateTime, 1, 4) = '")
                .append(filter.getYear())
                .append("'");
        }

        // month
        if (!"all".equals(filter.getMonth())) {
            query.append(" AND substring(e.dateTime, 6, 2) = '")
                .append(filter.getMonth())
                .append("'");
        }

        return entityManager
                .createQuery(query.toString(), Expense.class)
                .getResultList();
    }

   @Override
public double getTotalAmount() {
    return expenseRepository.findAll()
            .stream()
            .mapToInt(Expense::getAmount)
            .sum();
}

@Override
public long getTotalExpensesCount() {
    return expenseRepository.count();
}

@Override
public double getCurrentMonthTotal() {

    String currentMonth =
            java.time.LocalDate.now().toString().substring(0, 7); // yyyy-MM

    return expenseRepository.findAll()
            .stream()
            .filter(e ->
                e.getDateTime() != null &&
                e.getDateTime().startsWith(currentMonth)
            )
            .mapToInt(Expense::getAmount)
            .sum();
}

@Override
public Map<String, Double> getCategoryWiseTotal() {

    return expenseRepository.findAll()
            .stream()
            .filter(e -> e.getCategory() != null)
            .collect(
                java.util.stream.Collectors.groupingBy(
                    e -> e.getCategory().getName(),
                    java.util.stream.Collectors.summingDouble(Expense::getAmount)
                )
            );
}


}
