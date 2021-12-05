package com.operationsmiley.aarogyaecare.module;

public class Users {
    private String MLID2;
    private String blgrp;
    private String dob;
    private String fname;
    private String lname;
    private String gender;
    private String phone;
    private String country;
    private String city;
    private String count;
    private String topup;
    private String relation;
    private String usertype;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getMLID() {
        return MLID;
    }

    public void setMLID(String MLID) {
        this.MLID = MLID;
    }

    private String MLID;



    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBlgrp() {
        return blgrp;
    }

    public void setBlgrp(String blgrp) {
        this.blgrp = blgrp;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMLID2() {
        return MLID2;
    }

    public void setMLID2(String MLID2) {
        this.MLID2 = MLID2;
    }

    public Users() {
    }

    public String getTopup() {
        return topup;
    }

    public void setTopup(String topup) {
        this.topup = topup;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Users(String MLID2, String blgrp, String dob, String fname, String lname, String gender, String phone, String country, String city, String count, String topup, String relation, String usertype, String MLID) {
        this.MLID2 = MLID2;
        this.blgrp = blgrp;
        this.dob = dob;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.count = count;
        this.topup = topup;
        this.relation = relation;
        this.usertype = usertype;
        this.MLID = MLID;
    }
}
