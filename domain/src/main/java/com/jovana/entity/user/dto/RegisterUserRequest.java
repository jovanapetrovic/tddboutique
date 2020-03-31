package com.jovana.entity.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 18.03.2020
 */
public class RegisterUserRequest {

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull
    @Size(min = 6, max = 50)
    @Email(regexp = "^[a-zA-Z0-9_+]+(?:\\.[a-zA-Z0-9_+]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    private String email;

    @NotNull
    @Size(min = 6, max = 30)
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    @NotNull
    @Size(min = 6, max = 30)
    private String confirmPassword;

    public RegisterUserRequest() {
    }

    public static RegisterUserRequest createRegisterUserRequest(String firstName, String lastName, String email,
                                                                String username, String password, String confirmPassword) {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.firstName = firstName;
        registerUserRequest.lastName = lastName;
        registerUserRequest.email = email;
        registerUserRequest.username = username;
        registerUserRequest.password = password;
        registerUserRequest.confirmPassword = confirmPassword;
        return registerUserRequest;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
