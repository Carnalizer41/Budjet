package com.example.carnalizer.budjet;

public class Income {

    private String incomeDate;
    private float incomeAmount;
    private long dateInMilis;

    public Income(String incomeDate, float incomeAmount) {
        this.incomeDate = incomeDate;
        this.incomeAmount = incomeAmount;
    }

    public long getDateInMilis() {
        return dateInMilis;
    }

    public void setDateInMilis(long dateInMilis) {
        this.dateInMilis = dateInMilis;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public float getIncomeAmount() {
        return incomeAmount;
    }
}
