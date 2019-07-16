package com.example.carnalizer.budjet;

public class OverCat {

    private String name;
    private float currAmount;
    private float amount;

    public OverCat(String name, float currAmount, float amount) {
        this.name = name;
        this.currAmount = currAmount;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public float getCurrAmount() {
        return currAmount;
    }

    public float getAmount() {
        return amount;
    }
}
