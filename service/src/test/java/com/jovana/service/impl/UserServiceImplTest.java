package com.jovana.service.impl;

import com.jovana.entity.user.User;
import com.jovana.repositories.user.UserRepository;
import com.jovana.service.impl.user.UserService;
import com.jovana.service.impl.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final Long DEFAULT_USER_ID = 10L;
    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_PASSWORD = "test";

    @InjectMocks
    private UserService userService = new UserServiceImpl();
    @Mock
    private UserRepository userRepository;

    @Test
    public void test() throws Exception {
        // prepare
        User userMock = mock(User.class);

        when(userRepository.findByUsername(DEFAULT_USERNAME)).thenReturn(userMock);
        // exercise
        User userById = userService.getUserByUsername(DEFAULT_USERNAME);

        // verify
        Assertions.assertNotNull(userById);
    }

}