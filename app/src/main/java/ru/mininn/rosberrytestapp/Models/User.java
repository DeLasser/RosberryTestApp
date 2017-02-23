package ru.mininn.rosberrytestapp.Models;

import com.google.gson.Gson;

/**
 * It use GSON 2.8.0 library.
 */

public class User {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(new User(login, password));
    }

    static public User fromJson(String userJson) {
        Gson gson = new Gson();
        return gson.fromJson(userJson, User.class);
    }

    public boolean equals(User user){
        return (this.login.equals(user.getLogin()) && this.password.equals(user.getPassword()));
    }



    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
