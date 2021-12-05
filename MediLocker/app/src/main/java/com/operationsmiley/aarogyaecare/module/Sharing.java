package com.operationsmiley.aarogyaecare.module;

public class Sharing {
    private String access,email;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sharing() {
    }

    public Sharing(String access, String email) {
        this.access = access;
        this.email = email;
    }
}
