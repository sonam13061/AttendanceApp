package com.example.attendanceapp.model;

public class User {
    public String name;
    public String email;
    public String course;
    public String usertype;
    public int pin;
    public String nickname;

    public User(String name, String email, String course, String usertype, int pin, String nickname) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.usertype = usertype;
        this.pin = pin;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }


    public User() {
    }

    public User(String name, String email, String course, String usertype, int pin) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.usertype = usertype;
        this.pin = pin;
    }

    public User(String name, String email, String usertype, int pin) {
        this.name = name;
        this.email = email;
        this.usertype = usertype;
        this.pin = pin;
    }

    public User(String name, String email, String course, String usertype) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.usertype = usertype;
    }

    public String getName() {
        return name;
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

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", usertype='" + usertype + '\'' +
                ", pin=" + pin +
                '}';
    }
}
