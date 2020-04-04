package com.jovana.service.impl;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.entity.user.exception.EmailAlreadyExistsException;
import com.jovana.entity.user.exception.PasswordsDontMatchException;
import com.jovana.entity.user.exception.UsernameAlreadyExistsException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.user.UserRepository;
import com.jovana.service.util.RequestTestDataProvider;
import com.jovana.service.util.TestDataProvider;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.impl.user.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by jovana on 18.03.2020
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserService userService = new UserServiceImpl();
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("When we want to find a user by id")
    @Nested
    class GetUserTest {

        @DisplayName("Then user is fetched from database when id is valid")
        @Test
        public void testGetUserById() {
            // prepare
            Long testUserId = 10L;
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(User.class)));
            // exercise
            User newUser = userService.getUserById(testUserId);
            // verify
            assertNotNull(newUser, "User is null");
        }

        @DisplayName("Then error is thrown when user with passed id doesn't exist")
        @Test
        public void testGetUserByIdFailsWhenUserWithPassedIdDoesntExist() {
            // prepare
            Long testUserId = 9999L;
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            // verify
            assertThrows(EntityNotFoundException.class,
                    () -> userService.getUserById(testUserId), "User with id=" + testUserId + " doesn't exist");
        }
    }

    @DisplayName("When we want to register a new user")
    @Nested
    class RegisterUserTest {

        private static final String DEFAULT_ENCODED_PASSWORD = "$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366";

        private RegisterUserRequest registerUserRequest;
        private User user;

        @BeforeEach
        void setUp() {
            registerUserRequest = RequestTestDataProvider.getRegisterUserRequests().get("john");
            user = TestDataProvider.getUsers().get("john");
        }

        @DisplayName("Then a new user is created when valid RegisterUserRequest is passed")
        @Test
        public void testRegisterUserSuccess() {
            // prepare
            when(passwordEncoder.encode(any(String.class))).thenReturn(DEFAULT_ENCODED_PASSWORD);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

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
                    () -> assertEquals(DEFAULT_ENCODED_PASSWORD, newUser.getPassword(), "Password doesn't match")
            );
            verify(userRepository, times(1)).save(any(User.class));
        }

        @DisplayName("Then register fails when password and confirm password don't match")
        @Test
        public void testRegisterUserFailsWhenPasswordAndConfirmPasswordDontMatch() {
            // prepare
            RegisterUserRequest registerUserRequest = mock(RegisterUserRequest.class);
            when(registerUserRequest.getPassword()).thenReturn("123456");
            when(registerUserRequest.getConfirmPassword()).thenReturn("doesntmatch");

            // verify
            assertThrows(PasswordsDontMatchException.class,
                    () -> userService.registerUser(registerUserRequest), "Passwords don't match");
            verify(userRepository, times(0)).save(any(User.class));
        }

        @DisplayName("Then register fails when username already exists")
        @Test
        public void testRegisterUserFailsWhenUsernameAlreadyExists() {
            // prepare
            when(userRepository.findByUsername(any(String.class))).thenReturn(user);

            // verify
            assertThrows(UsernameAlreadyExistsException.class,
                    () -> userService.registerUser(registerUserRequest), "Username already exists");
            verify(userRepository, times(0)).save(any(User.class));
        }

        @DisplayName("Then register fails when email already exists")
        @Test
        public void testRegisterUserFailsWhenEmailAlreadyExists() {
            // prepare
            when(userRepository.findByEmail(any(String.class))).thenReturn(user);

            // verify
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.registerUser(registerUserRequest), "Email already exists");
            verify(userRepository, times(0)).save(any(User.class));
        }
    }

    @DisplayName("When we want to update user account")
    @Nested
    class UpdateUserAccountTest {

        private final Long TEST_USER_ID = 12L;
        private final String NEW_EMAIL_ADDRESS = "april_o_neal@test.com";
        private final String NEW_EMAIL_ADDRESS_IS_SAME_AS_OLD = "apriloneal@test.com";
        private final String NEW_EMAIL_ADDRESS_WHICH_EXISTS = "johndoe@test.com";

        private User user;
        private User updatedUser;

        @BeforeEach
        void setUp() {
            user = TestDataProvider.getUsers().get("april");
            updatedUser = TestDataProvider.getUsers().get("april");
            updatedUser.setEmail(NEW_EMAIL_ADDRESS);
        }

        @DisplayName("Then email is changed when new email is passed")
        @Test
        public void testChangeEmailAddressSuccess() {
            // prepare
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            when(userRepository.findByEmail(any(String.class))).thenReturn(null);

            // exercise
            userService.changeEmailAddress(TEST_USER_ID, NEW_EMAIL_ADDRESS);

            // verify
            User userAfter = userService.getUserById(TEST_USER_ID);
            assertEquals(NEW_EMAIL_ADDRESS, userAfter.getEmail(), "Email wasn't changed");
            verify(userRepository, times(1)).save(any(User.class));
        }

        @DisplayName("Then change email skips when new and old emails are equal")
        @Test
        public void testChangeEmailAddressSkipsWhenEmailAlreadyExists() {
            // prepare
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

            // exercise
            userService.changeEmailAddress(TEST_USER_ID, NEW_EMAIL_ADDRESS_IS_SAME_AS_OLD);

            // verify
            verify(userRepository, times(0)).save(any(User.class));
        }

        @DisplayName("Then change email fails when email already exists")
        @Test
        public void testChangeEmailAddressFailsWhenEmailAlreadyExists() {
            // prepare
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            when(userRepository.findByEmail(any(String.class))).thenReturn(mock(User.class));

            // verify
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.changeEmailAddress(TEST_USER_ID, NEW_EMAIL_ADDRESS_WHICH_EXISTS), "Email address already exists");
            verify(userRepository, times(0)).save(any(User.class));
        }

    }

}