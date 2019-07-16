package com.example.carnalizer.budjet;
public class Expense {
    private String date;
    private float amount;
    private String subcategory;
    public Expense(String date, float amount, String subcategory) {
        this.date = date;
        this.amount = amount;
        this.subcategory = subcategory;
    }
    public String getDate() {
        return date;
    }
    public float getAmount() {
        return amount;
    }
    public String getID_subcategory() {
        return subcategory;
    }
}
