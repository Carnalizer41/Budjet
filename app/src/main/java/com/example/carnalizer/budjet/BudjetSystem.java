package com.example.carnalizer.budjet;

public class BudjetSystem {
    private float regular;
    private float big;
    private float gifts;
    private float self;
    private float entertainment;
    public BudjetSystem()
    {
        regular = 0.6f;
        big = 0.1f;
        gifts = 0.1f;
        self = 0.1f;
        entertainment = 0.1f;
    }
    public float getRegular() {
        return regular;
    }
    public float getBig() {
        return big;
    }
    public float getGifts() {
        return gifts;
    }
    public float getSelf() {
        return self;
    }
    public float getEntertainment() {
        return entertainment;
    }
}
