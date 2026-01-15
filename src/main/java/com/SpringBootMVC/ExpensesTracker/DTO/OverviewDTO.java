package com.SpringBootMVC.ExpensesTracker.DTO;

import java.util.Map;
public class OverviewDTO {

    public double totalAmount;
    public long totalExpenses;
    public double currentMonthTotal;
    public Map<String, Double> categoryTotals;

    // getters & setters
    public double getTotalAmount(){
        return totalAmount;
    }

    public long getTotalExpenses(){
        return totalExpenses;
    }

    public double getCurrentMonthTotal(){
        return currentMonthTotal;
    }

    public Map<String, Double> getCategoryTotals() {
        return categoryTotals;
    }

    public void setTotalAmount(double totalAmount){
        this.totalAmount=totalAmount;
    }
    public void setTotalExpenses(long totalExpenses){
        this.totalExpenses=totalExpenses;
    }
    public void setCurrentMonthTotal(double currentMonthTotal){
        this.currentMonthTotal=currentMonthTotal;
    }

     public void setCategoryTotals(Map<String, Double> categoryTotals) {
        this.categoryTotals = categoryTotals;
    }
}
