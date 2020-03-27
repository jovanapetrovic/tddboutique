package com.jovana.entity.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jovana on 18.03.2020
 */
public class RegisterUserRequest {

    @NotNull(message = "First name must not be null")
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull(message = "Email must not be null")
    @Size(min = 6, max = 50)
    @Email
    private String email;

    @NotNull(message = "Username must not be null")
    @Size(min = 2, max = 30)
    private String username;

    @NotNull(message = "Password must not be null")
    @Size(min = 6, max = 30)
    private String password;

    @NotNull(message = "Password must not be null")
    @Size(min = 6, max = 30)
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
