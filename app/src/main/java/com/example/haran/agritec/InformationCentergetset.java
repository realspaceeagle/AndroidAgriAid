package com.example.haran.agritec;

public class InformationCentergetset {

    private String uid;
    private String time;
    private String date;
    private String description;
    private String profileimage;
    private String fullname;
    private String rating;

    public InformationCentergetset()
    {}
    public InformationCentergetset(String uid, String time, String date, String description, String profileimage, String fullname, String rating)
    {

        this.uid = uid;
        this.time = time;
        this.date = date;
        this.description=description;
        this.profileimage=profileimage;
        this.fullname=fullname;
        this.rating=rating;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
