package com.jovana.service.impl.user;

import com.jovana.entity.authority.AuthoritiesConstants;
import com.jovana.entity.authority.Authority;
import com.jovana.entity.user.User;
import com.jovana.entity.EntityNotFoundException;
import com.jovana.entity.user.UsernameAlreadyExistsException;
import com.jovana.repositories.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jovana on 24.02.2020
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
//        User user = userRepository.findOne(userId);
        User user = null;

        if (user == null) {
            LOGGER.info("User with id = {} was not found in the db.", userId);
            throw new EntityNotFoundException("No user found with id = " + userId);
        }

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOGGER.info("User with username {} was not found in the db.", username);
            throw new EntityNotFoundException("No user found. Username tried: " + username);
        }

        return user;
    }

    @Override
    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        validateUniqueUsername(registerUserRequest.getUsername());

        User user = new User();
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(AuthoritiesConstants.USER));

        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setAuthorities(authorities);
        user.setCreatedBy("system");

        userRepository.save(user);
    }

    private void validateUniqueUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException(username, "User already exists in the db with this username.");
        }
    }

}
