package com.jovana.auth;

import com.jovana.entity.user.User;
import com.jovana.exception.EntityNotFoundException;
import com.jovana.repositories.authority.AuthorityRepository;
import com.jovana.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jovana on 24.02.2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = null;

        try {
            user = getUserByUsername(username);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("No user found. Username tried: " + username);
        }

        Set<String> authorityNames = getAuthorityNamesByUserId(user.getId());
        Set<SimpleGrantedAuthority> authorities = authorityNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("No user found with username: " + username);
        }
        return user;
    }

    private Set<String> getAuthorityNamesByUserId(Long userId) {
        Set<String> authorityNames = authorityRepository.findAuthorityNamesByUserId(userId);
        if (authorityNames.isEmpty()) {
            throw new EntityNotFoundException("No authority found for user with id = " + userId);
        }
        return authorityNames;
    }
}
