package com.example.discoverbuc.Register2.HelperClasses;

public class UserHelperClass {

    String fullName, username, email, password, birthday;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String username, String email, String password, String birthday) {
        this.fullName = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}