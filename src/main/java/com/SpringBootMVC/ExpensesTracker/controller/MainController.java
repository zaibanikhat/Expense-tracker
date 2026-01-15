package com.SpringBootMVC.ExpensesTracker.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.service.CategoryService;
import com.SpringBootMVC.ExpensesTracker.service.ExpenseService;

@Controller
public class MainController {
    ExpenseService expenseService;
    CategoryService categoryService;

    @Autowired
    public MainController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }


    @GetMapping("/showAdd")
    public String addExpense(Model model){
        model.addAttribute("expense", new ExpenseDTO());
        return "add-expense";
    }

    @PostMapping("/submitAdd")
    public String submitAdd(@ModelAttribute("expense") ExpenseDTO expenseDTO){
        expenseService.save(expenseDTO);
        return "redirect:/list";
    }


   @GetMapping("/list")
public String list(Model model) {

    List<Expense> expenseList = expenseService.findAll();

    for (Expense expense : expenseList) {

        // category
        if (expense.getCategory() != null) {
            expense.setCategoryName(
                categoryService
                    .findCategoryById(expense.getCategory().getId())
                    .getName()
            );
        } else {
            expense.setCategoryName("Uncategorized");
        }

        // date & time
        if (expense.getDateTime() != null && !expense.getDateTime().isEmpty()) {
            LocalDateTime ldt =
                LocalDateTime.parse(
                    expense.getDateTime(),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                );

            expense.setDate(ldt.toLocalDate().toString());
            expense.setTime(ldt.toLocalTime().toString());
        }
    }

    model.addAttribute("expenseList", expenseList);
    model.addAttribute("filter", new FilterDTO());

    return "list-page";
}

  @GetMapping("/showUpdate")
    public String showUpdate(@RequestParam("expId") int id, Model model){

        Expense expense = expenseService.findExpenseById(id);

        if (expense == null) {
            throw new RuntimeException("Expense not found with id " + id);
        }

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDateTime(expense.getDateTime());

        if (expense.getCategory() != null) {
            expenseDTO.setCategory(expense.getCategory().getName());
        } else {
            expenseDTO.setCategory("");
        }

        model.addAttribute("expense", expenseDTO);
        model.addAttribute("expenseId", id);

        return "update-page";
    }


    @PostMapping("/submitUpdate")
    public String update(@RequestParam("expId") int id,
                        @ModelAttribute("expense") ExpenseDTO expenseDTO){
        expenseDTO.setExpenseId(id);
        expenseService.update(expenseDTO);
        return "redirect:/list";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("expId") int id){
        expenseService.deleteExpenseById(id);
        return "redirect:/list";
    }


    @PostMapping("/processFilter")
    public String processFilter(@ModelAttribute("filter") FilterDTO filter, Model model) {

        List<Expense> expenseList = expenseService.findFilterResult(filter);

        for (Expense expense : expenseList) {

            // category name
            if (expense.getCategory() != null) {
                expense.setCategoryName(
                    categoryService
                        .findCategoryById(expense.getCategory().getId())
                        .getName()
                );
            } else {
                expense.setCategoryName("Uncategorized");
            }

            // date & time (THIS WAS MISSING / FAILING)
            if (expense.getDateTime() != null && !expense.getDateTime().isEmpty()) {

                LocalDateTime ldt =
                    LocalDateTime.parse(
                        expense.getDateTime(),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    );

                expense.setDate(ldt.toLocalDate().toString());
                expense.setTime(ldt.toLocalTime().toString());
            }
        }

        model.addAttribute("expenseList", expenseList);
        return "filter-result";
    }


}
