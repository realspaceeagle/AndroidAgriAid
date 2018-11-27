package com.example.haran.agritec;

public class FindFriends
{
    public String profileimage,fullname,about;

    public FindFriends()
    {}

    public FindFriends(String profileimage, String fullname, String about) {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.about = about;
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

    public String getStatus() {
        return about;
    }

    public void setAbout(String status) {
        this.about = status;
    }
}
