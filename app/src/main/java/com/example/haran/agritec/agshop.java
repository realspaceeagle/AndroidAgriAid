package com.example.haran.agritec;

public class  agshop {

   private String uid;
    private String time;
    private String date;
    private String postimage;
    private String description;
    private String profileimage;
    private String fullname;
    private String Item_name;
    private String Price;
    private String Offers;
    private String Services;

    public  agshop()
    {

    }

    public  agshop(String uid, String time, String date, String postimage, String description, String profileimage, String fullname,String Item_name,String Price,String Offers,String Services) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.Item_name=Item_name;
        this.Price=Price;
        this.Offers=Offers;
        this.Services=Services;
    }

    public String getItem_name() { return Item_name; }

    public void setItem_name(String Item_name) { this.Item_name = Item_name; }

    public String getPrice() { return Price; }
    public void setPrice(String Price) { this.Price = Price; }

    public String getOffers(){ return Offers; }

    public void setOffers(String Offers) { this.Offers = Offers; }

    public String getServices() { return Services; }

    public void setServices(String Services){ this.Services = Services; }


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

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
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
}