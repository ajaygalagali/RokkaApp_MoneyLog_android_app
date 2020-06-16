package com.astro.rokka;

public class HomeList{
    private int balance;
    private String labName;

    public HomeList(int balance,String labName){
        this.balance = balance;
        this.labName = labName;

    }

    public int getBalance() {
        return balance;
    }

    public String getLabName() {
        return labName;
    }


    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }


}
