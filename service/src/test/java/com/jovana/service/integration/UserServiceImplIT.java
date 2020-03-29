package com.jovana.service.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.entity.user.exception.EmailAlreadyExistsException;
import com.jovana.entity.user.exception.PasswordsDontMatchException;
import com.jovana.entity.user.exception.UsernameAlreadyExistsException;
import com.jovana.service.impl.user.UserService;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by jovana on 29.03.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.userservice"})
public class UserServiceImplIT extends AbstractTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("When we want to register a new user")
    @Nested
    class RegisterUserITest {

        private RegisterUserRequest registerUserRequest;
        private RegisterUserRequest wrongPasswordRegisterUserRequest;
        private RegisterUserRequest usernameExistsRegisterUserRequest;
        private RegisterUserRequest emailExistsRegisterUserRequest;

        @BeforeEach
        void setUp() {
            registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                    "John",
                    "Doe",
                    "johndoe@test.com",
                    "johndoe",
                    "123456",
                    "123456");
            wrongPasswordRegisterUserRequest = RegisterUserRequest.createRegisterUserRequest(
                    "Jane",
                    "Doe",
                    "janedoe@test.com",
                    "janedoe",
                    "123456",
                    "doesntmatch");
            usernameExistsRegisterUserRequest = RegisterUserRequest.createRegisterUserRequest(
                    "firstname3",
                    "lastname3",
                    "testuser33@test.com",
                    "testuser3",
                    "123456",
                    "123456");
            emailExistsRegisterUserRequest = RegisterUserRequest.createRegisterUserRequest(
                    "firstname3",
                    "lastname3",
                    "testuser3@test.com",
                    "testuser33",
                    "123456",
                    "123456");
        }

        @DisplayName("Then a new user is created when valid RegisterUserRequest is passed")
        @Test
        public void testRegisterUserSuccess() {
            // exercise
            Long userId = userService.registerUser(registerUserRequest);

            // verify
            User newUser = userService.getUserById(userId);

            assertAll("Verify register user request",
                    () -> Assertions.assertNotNull(registerUserRequest, "RegisterUserRequest is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getFirstName(), "First name is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getLastName(), "Last name is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getEmail(), "Email is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getUsername(), "Username is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getPassword(), "Password is null"),
                    () -> Assertions.assertNotNull(registerUserRequest.getConfirmPassword(), "Confirm password is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getPassword(), registerUserRequest.getConfirmPassword())
            );

            assertAll("Verify registered user",
                    () -> Assertions.assertNotNull(newUser, "User is null"),
                    () -> Assertions.assertNotNull(newUser.getFirstName(), "First name is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getFirstName(), newUser.getFirstName(), "First name doesn't match"),
                    () -> Assertions.assertNotNull(newUser.getLastName(), "Last name is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getLastName(), newUser.getLastName(), "Last name doesn't match"),
                    () -> Assertions.assertNotNull(newUser.getEmail(), "Email is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getEmail(), newUser.getEmail(), "Email doesn't match"),
                    () -> Assertions.assertNotNull(newUser.getUsername(), "Username is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getUsername(), newUser.getUsername(), "Username doesn't match"),
                    () -> Assertions.assertNotNull(newUser.getPassword(), "Password is null"),
                    () -> Assertions.assertTrue(passwordEncoder.matches(registerUserRequest.getPassword(), newUser.getPassword()))
            );
        }

        @DisplayName("Then register fails when password and confirm password don't match")
        @Test
        public void testRegisterUserFailsWhenPasswordAndConfirmPasswordDontMatch() {
            // verify
            assertThrows(PasswordsDontMatchException.class,
                    () -> userService.registerUser(wrongPasswordRegisterUserRequest), "Passwords don't match");
        }

        @DisplayName("Then register fails when username already exists")
        @Test
        public void testRegisterUserFailsWhenUsernameAlreadyExists() {
            // verify
            assertThrows(UsernameAlreadyExistsException.class,
                    () -> userService.registerUser(usernameExistsRegisterUserRequest), "Username already exists");
        }

        @DisplayName("Then register fails when email already exists")
        @Test
        public void testRegisterUserFailsWhenEmailAlreadyExists() {
            // verify
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.registerUser(emailExistsRegisterUserRequest), "Email already exists");
        }
    }

}
