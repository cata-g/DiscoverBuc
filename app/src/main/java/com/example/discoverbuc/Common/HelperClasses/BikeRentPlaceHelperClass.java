package com.example.discoverbuc.Common.HelperClasses;

public class BikeRentPlaceHelperClass {

    String name;
    double lat, lng;

    public BikeRentPlaceHelperClass(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
