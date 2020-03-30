package com.jovana.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Sets;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.authority.AuthorityConstants;
import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.entity.user.exception.EmailAlreadyExistsException;
import com.jovana.entity.user.exception.PasswordsDontMatchException;
import com.jovana.entity.user.exception.UsernameAlreadyExistsException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.user.UserRepository;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.impl.user.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Created by jovana on 18.03.2020
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @DisplayName("When we want to find a user by id")
    @Nested
    class GetUserTest {

        private final Long USER_ID_EXISTS = 10L;
        private final Long USER_ID_NOT_EXISTS = 9999L;

        @InjectMocks
        private UserService userService = new UserServiceImpl();
        @Mock
        private UserRepository userRepository;

        @DisplayName("Then user is fetched from database")
        @Test
        public void testGetUserById() {
            // prepare
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(User.class)));
            // exercise
            User newUser = userService.getUserById(USER_ID_EXISTS);
            // verify
            Assertions.assertNotNull(newUser, "User is null");
        }

        @DisplayName("Then error is thrown when user with passed id doesn't exist")
        @Test
        public void testGetUserByIdFailsWhenUserWithPassedIdDoesntExist() {
            // prepare
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> userService.getUserById(USER_ID_NOT_EXISTS), "User with id=" + USER_ID_NOT_EXISTS + " doesn't exist");
        }
    }

    @DisplayName("When we want to register a new user")
    @Nested
    class RegisterUserTest {

        private static final String DEFAULT_ENCODED_PASSWORD = "$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366";

        @InjectMocks
        private UserService userService = new UserServiceImpl();
        @Mock
        private UserRepository userRepository;
        @Mock
        private BCryptPasswordEncoder passwordEncoder;

        private RegisterUserRequest registerUserRequest;
        private RegisterUserRequest wrongPasswordRegisterUserRequest;
        private User user;

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
                    "John",
                    "Doe",
                    "johndoe@test.com",
                    "johndoe",
                    "123456",
                    "doesntmatch");
            user = User.createUser(
                    "John",
                    "Doe",
                    "johndoe@test.com",
                    "johndoe",
                    DEFAULT_ENCODED_PASSWORD,
                    Sets.newHashSet(new Authority(AuthorityConstants.USER)));
        }

        @DisplayName("Then a new user is created when valid RegisterUserRequest is passed")
        @Test
        public void testRegisterUserSuccess() {
            // prepare
            when(passwordEncoder.encode(any(String.class))).thenReturn(DEFAULT_ENCODED_PASSWORD);
            when(userRepository.save(any(User.class))).thenReturn(user);

            // exercise
            userService.registerUser(registerUserRequest);

            // verify
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
                    () -> Assertions.assertNotNull(user, "User is null"),
                    () -> Assertions.assertNotNull(user.getFirstName(), "First name is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getFirstName(), user.getFirstName(), "First name doesn't match"),
                    () -> Assertions.assertNotNull(user.getLastName(), "Last name is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getLastName(), user.getLastName(), "Last name doesn't match"),
                    () -> Assertions.assertNotNull(user.getEmail(), "Email is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getEmail(), user.getEmail(), "Email doesn't match"),
                    () -> Assertions.assertNotNull(user.getUsername(), "Username is null"),
                    () -> Assertions.assertEquals(registerUserRequest.getUsername(), user.getUsername(), "Username doesn't match"),
                    () -> Assertions.assertNotNull(user.getPassword(), "Password is null"),
                    () -> Assertions.assertEquals(DEFAULT_ENCODED_PASSWORD, user.getPassword(), "Password doesn't match")
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
            // prepare
            when(userRepository.findByUsername(any(String.class))).thenReturn(user);
            // verify
            assertThrows(UsernameAlreadyExistsException.class,
                    () -> userService.registerUser(registerUserRequest), "Username already exists");
        }

        @DisplayName("Then register fails when email already exists")
        @Test
        public void testRegisterUserFailsWhenEmailAlreadyExists() {
            // prepare
            when(userRepository.findByEmail(any(String.class))).thenReturn(user);
            // verify
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.registerUser(registerUserRequest), "Email already exists");
        }
    }

}