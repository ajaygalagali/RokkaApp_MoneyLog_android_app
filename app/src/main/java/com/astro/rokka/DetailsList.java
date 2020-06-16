package com.astro.rokka;

public class DetailsList {
    private int number;
    private String time;

    public DetailsList(int number,String time){
        this.number=number;
        this.time=time;
    }

    public int getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
