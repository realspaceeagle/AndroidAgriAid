package com.example.haran.agritec;

public class Messages
{
    public String date;
    private String Time;
    public String from;
    public String message;
    public String type;

    public Messages()
    {


    }
    public Messages(String date,String Time,String from,String message,String type)
    {
        this.date=date;
        this.Time=Time;
        this.type=type;
        this.message=message;
        this.from=from;

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
