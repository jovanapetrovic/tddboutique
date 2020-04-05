package com.jovana.service.integration;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.ChangeEmailAddressRequest;
import com.jovana.entity.user.dto.ChangePasswordRequest;
import com.jovana.entity.user.dto.ChangeUsernameRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.entity.user.exception.EmailAlreadyExistsException;
import com.jovana.entity.user.exception.UsernameAlreadyExistsException;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.util.RequestTestDataProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jovana on 29.03.2020
 */
@FlywayTest(locationsForMigrate = {"db.additional.userservice"})
public class UserServiceImplIT extends AbstractTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("When we want to find a user by id")
    @Nested
    class GetUserTest {

        private final Long TEST_USER_ID = 10L;

        @DisplayName("Then user is fetched from database when id is valid")
        @Test
        public void testGetUserById() {
            // exercise
            User newUser = userService.getUserById(TEST_USER_ID);
            // verify
            assertNotNull(newUser, "User is null");
        }
    }

    @DisplayName("When we want to register a new user")
    @Nested
    class RegisterUserITest {

        private RegisterUserRequest registerUserRequest;

        @BeforeEach
        void setUp() {
            registerUserRequest = RequestTestDataProvider.getRegisterUserRequests().get("john");
        }

        @DisplayName("Then a new user is created when valid RegisterUserRequest is passed")
        @Test
        public void testRegisterUserSuccess() {
            // exercise
            Long userId = userService.registerUser(registerUserRequest);

            // verify
            User newUser = userService.getUserById(userId);

            assertAll("Verify registered user",
                    () -> assertNotNull(newUser, "User is null"),
                    () -> assertNotNull(newUser.getFirstName(), "First name is null"),
                    () -> assertEquals(registerUserRequest.getFirstName(), newUser.getFirstName(), "First name doesn't match"),
                    () -> assertNotNull(newUser.getLastName(), "Last name is null"),
                    () -> assertEquals(registerUserRequest.getLastName(), newUser.getLastName(), "Last name doesn't match"),
                    () -> assertNotNull(newUser.getEmail(), "Email is null"),
                    () -> assertEquals(registerUserRequest.getEmail(), newUser.getEmail(), "Email doesn't match"),
                    () -> assertNotNull(newUser.getUsername(), "Username is null"),
                    () -> assertEquals(registerUserRequest.getUsername(), newUser.getUsername(), "Username doesn't match"),
                    () -> assertNotNull(newUser.getPassword(), "Password is null"),
                    () -> assertTrue(passwordEncoder.matches(registerUserRequest.getPassword(), newUser.getPassword()))
            );

        }

    }

    @DisplayName("When we want to update user account")
    @Nested
    class UpdateUserAccountTest {

        @DisplayName("Then email is changed when valid new email is passed")
        @Test
        public void testChangeEmailAddressSuccess() {
            // prepare
            Long TEST_USER_ID = 13L;

            // exercise
            String newEmailAddress = "test_user_4@test.com";
            ChangeEmailAddressRequest changeEmailAddressRequest = new ChangeEmailAddressRequest();
            changeEmailAddressRequest.setNewEmailAddress(newEmailAddress);
            userService.changeEmailAddress(TEST_USER_ID, changeEmailAddressRequest);

            // verify
            User user = userService.getUserById(TEST_USER_ID);
            assertEquals(newEmailAddress, user.getEmail(), "Email wasn't changed");
        }

        @DisplayName("Then change email fails when email already exists")
        @Test
        public void testChangeEmailAddressFailsWhenEmailAlreadyExists() {
            // prepare
            Long TEST_USER_ID = 13L;
            User userBefore = userService.getUserById(TEST_USER_ID);

            // exercise
            String newEmailAddress = "testuser3@test.com";
            ChangeEmailAddressRequest changeEmailAddressRequest = new ChangeEmailAddressRequest();
            changeEmailAddressRequest.setNewEmailAddress(newEmailAddress);

            // verify
            User userAfter = userService.getUserById(TEST_USER_ID);
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.changeEmailAddress(TEST_USER_ID, changeEmailAddressRequest), "Email address already exists");
            assertEquals(userBefore.getEmail(), userAfter.getEmail());
        }

        @DisplayName("Then username is changed when valid new username is passed")
        @Test
        public void testChangeUsernameSuccess() {
            // prepare
            Long TEST_USER_ID = 14L;

            // exercise
            String newUsername = "test_user_5";
            ChangeUsernameRequest changeUsernameRequest = new ChangeUsernameRequest();
            changeUsernameRequest.setUsername(newUsername);
            userService.changeUsername(TEST_USER_ID, changeUsernameRequest);

            // verify
            User user = userService.getUserById(TEST_USER_ID);
            assertEquals(newUsername, user.getUsername(), "Username wasn't changed");
        }

        @DisplayName("Then change username fails when username already exists")
        @Test
        public void testChangeUsernameFailsWhenUsernameAlreadyExists() {
            // prepare
            Long TEST_USER_ID = 14L;
            User userBefore = userService.getUserById(TEST_USER_ID);

            // exercise
            String newUsername = "testuser3";
            ChangeUsernameRequest changeUsernameRequest = new ChangeUsernameRequest();
            changeUsernameRequest.setUsername(newUsername);

            // verify
            User userAfter = userService.getUserById(TEST_USER_ID);
            assertThrows(UsernameAlreadyExistsException.class,
                    () -> userService.changeUsername(TEST_USER_ID, changeUsernameRequest), "Username already exists");
            assertEquals(userBefore.getUsername(), userAfter.getUsername());
        }

        @DisplayName("Then password is changed when valid new password is passed")
        @Test
        public void testChangePasswordSuccess() {
            // prepare
            Long TEST_USER_ID = 15L;

            // exercise
            String newPassword = "password123";
            String confirmPassword = "password123";
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
            changePasswordRequest.setPassword(newPassword);
            changePasswordRequest.setConfirmPassword(confirmPassword);
            userService.changePassword(TEST_USER_ID, changePasswordRequest);

            // verify
            User user = userService.getUserById(TEST_USER_ID);
            assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
        }

    }

}
