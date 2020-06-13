package com.astro.rokka;

public class HomeList{
    private int balance;
    private String labName;
    private int plusID;
    private int minusID;
//        private  String sign;

    public HomeList(int balance,String labName, int plusID, int minusID){
        this.balance = balance;
        this.labName = labName;
        this.plusID = plusID;
        this.minusID = minusID;
//            this.sign = sign;
    }

    public int getBalance() {
        return balance;
    }

    public String getLabName() {
        return labName;
    }

    public int getPlusID() {
        return plusID;
    }

    public int getMinusID() {
        return minusID;
    }

    /*public String getSign() {
        return sign;
    }
*/
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public void setMinusID(int minusID) {
        this.minusID = minusID;
    }

    public void setPlusID(int plusID) {
        this.plusID = plusID;
    }

        /*public void setSign(String sign) {
            this.sign = sign;
        }*/
}
