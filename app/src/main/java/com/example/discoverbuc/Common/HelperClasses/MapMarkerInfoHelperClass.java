package com.example.discoverbuc.Common.HelperClasses;

import android.content.Context;
import android.content.Intent;

import com.example.discoverbuc.Menu.PopActivity;

public class MapMarkerInfoHelperClass {

    Context context;
    int clickCount;
    String tag, catTag, title, desc;
    float rating;
    int imageLoc;


    public MapMarkerInfoHelperClass(int clickCount, String tag, String catTag, String title, String desc, float rating, int imageLoc, Context context) {
        this.clickCount = clickCount;
        this.tag = tag;
        this.catTag = catTag;
        this.title = title;
        this.desc = desc;
        this.rating = rating;
        this.imageLoc = imageLoc;
        this.context = context;
    }
    public MapMarkerInfoHelperClass(int clickCount, String catTag, String title, Context context) {
        this.context = context;
        this.title = title;
        this.catTag = catTag;
        this.clickCount = clickCount;
    }


    public int getClickCount() {
        return clickCount;
    }

    public String getTag() {
        return tag;
    }

    public String getCatTag() {
        return catTag;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public float getRating() {
        return rating;
    }

    public int getImageLoc() {
        return imageLoc;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void openPop(){
        Intent intent = new Intent(context, PopActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("imageRes", imageLoc);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        intent.putExtra("rating", rating);
        intent.putExtra("category", catTag);
        context.startActivity(intent);
    }
}
