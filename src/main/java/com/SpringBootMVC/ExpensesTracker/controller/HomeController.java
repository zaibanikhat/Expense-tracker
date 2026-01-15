package com.SpringBootMVC.ExpensesTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.SpringBootMVC.ExpensesTracker.DTO.OverviewDTO;
import com.SpringBootMVC.ExpensesTracker.service.ExpenseService;



@Controller
public class HomeController {

      private final ExpenseService expenseService;

        @Autowired
        public HomeController(ExpenseService expenseService)
        {
            this.expenseService=expenseService;
        }

    @GetMapping("/")
    public String home(Model model) {
      

        OverviewDTO overviewDTO = new OverviewDTO();
        overviewDTO.setTotalAmount(expenseService.getTotalAmount());
        overviewDTO.setTotalExpenses(expenseService.getTotalExpensesCount());
        overviewDTO.setCategoryTotals(expenseService.getCategoryWiseTotal());
        overviewDTO.setCurrentMonthTotal(expenseService.getCurrentMonthTotal());

        model.addAttribute("overview",overviewDTO);

        return "landing-page";
    }
}

