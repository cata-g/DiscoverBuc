package com.example.discoverbuc.Menu.HelperClasses;

import android.util.Log;

public class CardHelperClass {

    int imageSrc, wishedSrc;
    float rating;
    String title, desc, tag, categoryTag;

    public CardHelperClass(int imageSrc, float rating, String title, String desc, String tag, String categoryTag, int wishedSrc) {
        this.imageSrc = imageSrc;
        this.rating = rating;
        this.title = title;
        this.desc = desc;
        this.tag = tag;
        this.categoryTag = categoryTag;
        this.wishedSrc= wishedSrc;
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

    public String getCategoryTag() {
        return categoryTag;
    }

    public int getWishedSrc() {
        return wishedSrc;
    }

    public void setWishedSrc(int wishedSrc) {
        this.wishedSrc = wishedSrc;
    }
}
