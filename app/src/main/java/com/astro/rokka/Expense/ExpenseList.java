package com.astro.rokka.Expense;

public class ExpenseList {
    private int amount;
    private String note;
    private String date;
    private int id;

    public ExpenseList(int amount, String note, String date,int id) {
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
