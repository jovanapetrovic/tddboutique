package com.jovana.service.impl.user;

import com.google.common.collect.Sets;
import com.jovana.entity.authority.AuthorityConstants;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.ChangeEmailAddressRequest;
import com.jovana.entity.user.dto.ChangePasswordRequest;
import com.jovana.entity.user.dto.ChangeUsernameRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.entity.user.exception.EmailAlreadyExistsException;
import com.jovana.entity.user.exception.PasswordsDontMatchException;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.entity.user.exception.UsernameAlreadyExistsException;
import com.jovana.repositories.user.UserRepository;
import com.jovana.service.security.IsAdminOrUser;
import com.jovana.service.security.IsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Created by jovana on 24.02.2020
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @IsAdminOrUser
    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            LOGGER.info("User with id = {} was not found in the db.", userId);
            throw new EntityNotFoundException("No user found with id = " + userId);
        }
        return user.get();
    }

    @Override
    @Transactional
    public Long registerUser(RegisterUserRequest registerUserRequest) {
        validatePasswords(registerUserRequest.getPassword(), registerUserRequest.getConfirmPassword());
        validateUsername(registerUserRequest.getUsername());
        validateEmail(registerUserRequest.getEmail());

        User user = new User();
        user.setFirstName(registerUserRequest.getFirstName());
        user.setLastName(registerUserRequest.getLastName());
        user.setEmail(registerUserRequest.getEmail());
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setAuthorities(Sets.newHashSet(new Authority(AuthorityConstants.USER)));

        User newUser = userRepository.save(user);
        return newUser.getId();
    }

    @IsUser
    @Override
    public void changeEmailAddress(Long userId, ChangeEmailAddressRequest changeEmailAddressRequest) {
        User user = getUserById(userId);
        if (!user.getEmail().equals(changeEmailAddressRequest.getNewEmailAddress())) {
            validateEmail(changeEmailAddressRequest.getNewEmailAddress());
            user.setEmail(changeEmailAddressRequest.getNewEmailAddress());
            userRepository.save(user);
        }
    }

    @IsUser
    @Override
    public void changeUsername(Long userId, ChangeUsernameRequest changeUsernameRequest) {
        User user = getUserById(userId);
        if (!user.getUsername().equals(changeUsernameRequest.getUsername())) {
            validateUsername(changeUsernameRequest.getUsername());
            user.setUsername(changeUsernameRequest.getUsername());
            userRepository.save(user);
        }
    }

    @IsUser
    @Override
    public void changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        validatePasswords(changePasswordRequest.getPassword(), changePasswordRequest.getConfirmPassword());
        User user = getUserById(userId);
        if (!passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
            userRepository.save(user);
        }
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsDontMatchException("Password and confirm password don't match.");
        }
    }

    private void validateUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException(username, "User already exists in the db with this email.");
        }
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException(email, "User already exists in the db with this email.");
        }
    }

}
