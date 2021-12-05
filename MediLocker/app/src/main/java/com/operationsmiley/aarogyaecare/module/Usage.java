package com.operationsmiley.aarogyaecare.module;

public class Usage {
    private String total,used;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public Usage() {
    }

    public Usage(String total, String used) {
        this.total = total;
        this.used = used;
    }
}
