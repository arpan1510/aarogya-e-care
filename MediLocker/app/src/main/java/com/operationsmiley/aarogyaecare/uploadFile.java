package com.operationsmiley.aarogyaecare;

public class uploadFile {

    public String filename;
    public String record_type;
    public String doctorname;
    public String hospital;
    public String notes;
    public String date;
    public String size;
    public String userwho;
    public String filetype;
    public String record_type1;
    public String record_type2;
    public String original_filename;
    public String url;
    public String date_o;

    public String getDate_o() {
        return date_o;
    }

    public void setDate_o(String date_o) {
        this.date_o = date_o;
    }

    public String getRecord_type1() {
        return record_type1;
    }

    public void setRecord_type1(String record_type1) {
        this.record_type1 = record_type1;
    }

    public String getRecord_type2() {
        return record_type2;
    }

    public void setRecord_type2(String record_type2) {
        this.record_type2 = record_type2;
    }

    public uploadFile(String filename, String record_type, String doctorname, String hospital, String notes, String date, String size, String userwho, String filetype, String record_type1, String record_type2, String original_filename, String url, String date_o) {
        this.filename = filename;
        this.record_type = record_type;
        this.doctorname = doctorname;
        this.hospital = hospital;
        this.notes = notes;
        this.date = date;
        this.size = size;
        this.userwho = userwho;
        this.filetype = filetype;
        this.record_type1 = record_type1;
        this.record_type2 = record_type2;
        this.original_filename = original_filename;
        this.url = url;
        this.date_o = date_o;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }



    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public uploadFile() {
    }

    public String getUserwho() {
        return userwho;
    }

    public void setUserwho(String userwho) {
        this.userwho = userwho;
    }




}