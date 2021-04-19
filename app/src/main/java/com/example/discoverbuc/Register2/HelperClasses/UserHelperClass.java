package com.example.discoverbuc.Register2.HelperClasses;

public class UserHelperClass {

    String fullName, username, password, birthday, phone;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String username, String password, String birthday, String phone) {
        this.fullName = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
