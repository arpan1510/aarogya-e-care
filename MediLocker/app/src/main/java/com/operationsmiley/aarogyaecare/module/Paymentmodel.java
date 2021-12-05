package com.operationsmiley.aarogyaecare.module;

public class Paymentmodel {

    private String amount,category,date,email_id,order_id;
    private int status;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Paymentmodel(String amount, String category, String date, String email_id, String order_id, int status) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.email_id = email_id;
        this.order_id = order_id;
        this.status = status;
    }

    public Paymentmodel() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
