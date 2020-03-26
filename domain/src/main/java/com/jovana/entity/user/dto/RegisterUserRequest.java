package com.jovana.entity.user.dto;

/**
 * Created by jovana on 18.03.2020
 */
public class RegisterUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;

    private RegisterUserRequest() {
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

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

}
