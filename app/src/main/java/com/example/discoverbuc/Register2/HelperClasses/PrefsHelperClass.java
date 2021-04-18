package com.example.discoverbuc.Register2.HelperClasses;

public class PrefsHelperClass {

    int natureSelected, museumSelected, restaurantSelected, coffee_shopSelected;

    public PrefsHelperClass() {
    }

    public PrefsHelperClass(int natureSelected, int museumSelected, int restaurantSelected, int coffee_shopSelected) {
        this.natureSelected = natureSelected;
        this.museumSelected = museumSelected;
        this.restaurantSelected = restaurantSelected;
        this.coffee_shopSelected = coffee_shopSelected;
    }

    public boolean getNatureSelected() {
        return natureSelected == 1;
    }

    public boolean getMuseumSelected() {
        return museumSelected == 1;
    }

    public boolean getRestaurantSelected() {
        return restaurantSelected == 1;
    }

    public boolean getCoffee_shopSelected() {
        return coffee_shopSelected == 1;
    }

    public void setNatureSelected(int natureSelected) {
        this.natureSelected = natureSelected;
    }

    public void setMuseumSelected(int museumSelected) {
        this.museumSelected = museumSelected;
    }

    public void setRestaurantSelected(int restaurantSelected) {
        this.restaurantSelected = restaurantSelected;
    }

    public void setCoffee_shopSelected(int coffee_shopSelected) {
        this.coffee_shopSelected = coffee_shopSelected;
    }
}
