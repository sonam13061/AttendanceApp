package com.example.attendanceapp.model;

public class Student {
    public String name;
    public String email;
    public String course;
    public String type;

    public String getUsertype() {
        return type;
    }

    public void setUsertype(String usertype) {
        this.type = usertype;
    }

    public Student(String name, String email, String course, String usertype) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.type = usertype;
    }

    public String getName() {
        return name;
    }

    public Student() {
    }

    public Student(String name, String email, String course) {
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
