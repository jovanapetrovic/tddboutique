package com.jovana.service.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.util.RequestTestDataProvider;
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
            Assertions.assertNotNull(newUser, "User is null");
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

    }

}
