package com.jovana.service.impl.user;

/**
 * Created by jovana on 24.02.2020
 */
public class RegisterUserRequest {

    private String username;
    private String password;

    public RegisterUserRequest() {
        super();
    }

    public RegisterUserRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
