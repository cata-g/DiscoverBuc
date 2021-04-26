package com.example.discoverbuc.Menu.HelperClasses;

public class CardHelperClass {

    int imageSrc;
    float rating;
    String title, desc, tag;

    public CardHelperClass(int imageSrc, float rating, String title, String desc, String tag) {
        this.imageSrc = imageSrc;
        this.rating = rating;
        this.title = title;
        this.desc = desc;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }
}
