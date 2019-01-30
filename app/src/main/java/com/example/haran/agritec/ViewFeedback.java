package com.example.haran.agritec;

import android.view.View;

public class ViewFeedback {

    private String Complaint;
    private String Email;
    private String Feedback;
    private String Name;
    private String Rating;


    public ViewFeedback()
    {

    }
    public ViewFeedback(String Complaint,String Email,String Feedback,String Name,String Rating)
    {
        this.Complaint=Complaint;
        this.Email=Email;
        this.Feedback=Feedback;
        this.Name=Name;
        this.Rating=Rating;

    }

    public String getComplaint() {
        return Complaint;
    }

    public void setComplaint(String complaint) {
        Complaint = complaint;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }






}
