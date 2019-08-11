package com.example.attendanceapp.model;

public class Attendance {
    String date,time,subject,userid,name,date_userid;

    public Attendance(String date, String time, String subject, String userid, String name, String date_userid) {
        this.date = date;
        this.time = time;
        this.subject = subject;
        this.userid = userid;
        this.name = name;
        this.date_userid = date_userid;
    }

    public Attendance() {
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", subject='" + subject + '\'' +
                ", userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", date_userid='" + date_userid + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_userid() {
        return date_userid;
    }

    public void setDate_userid(String date_userid) {
        this.date_userid = date_userid;
    }
}
