package com.astro.rokka;

public class DetailsList {

    private String time;
    private int total_wages;
    private int paid_wages;
    private int days;
    private int rem_wages;

    private String note;


    public DetailsList(int days, int total_wages,int rem_wages,int paid_wages,String note,String time){

        this.time = time;
        this.days = days;
        this.paid_wages = paid_wages;
        this.total_wages = total_wages;
        this.rem_wages = rem_wages;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public int getDays() {
        return days;
    }

    public int getPaid_wages() {
        return paid_wages;
    }

    public int getRem_wages() {
        return rem_wages;
    }

    public int getTotal_wages() {
        return total_wages;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setPaid_wages(int paid_wages) {
        this.paid_wages = paid_wages;
    }

    public void setTotal_wages(int total_wages) {
        this.total_wages = total_wages;
    }

    public void setRem_wages(int rem_wages) {
        this.rem_wages = rem_wages;
    }

}
