package com.example.discoverbuc.Menu.HelperClasses;

public class CardHelperClass {

    int imageSrc;
    float rating;
    String title,desc;

    public CardHelperClass(int imageSrc, float rating, String title, String desc) {
        this.imageSrc = imageSrc;
        this.rating = rating;
        this.title = title;
        this.desc = desc;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public float getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
