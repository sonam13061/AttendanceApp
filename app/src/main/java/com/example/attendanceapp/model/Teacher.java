package com.example.attendanceapp.model;

public class Teacher {
    public String nm;
    public String em;
    public String pin;

    public String getType() {
        return type;
    }

    public Teacher(String nm, String em, String pin, String type) {
        this.nm = nm;
        this.em = em;
        this.pin = pin;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  String type;

    public Teacher() {
    }

    public Teacher(String nm, String em, String pin) {
        this.nm = nm;
        this.em = em;
        this.pin = pin;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "nm='" + nm + '\'' +
                ", em='" + em + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
