package com.jovana.auth;

import com.jovana.entity.authority.Authority;
import com.jovana.entity.user.User;
import com.jovana.entity.EntityNotFoundException;
import com.jovana.service.impl.authority.AuthorityService;
import com.jovana.service.impl.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jovana on 24.02.2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = null;

        try {
            user = userService.getUserByUsername(username); // we are checking if username=login
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("No user found. Username tried: " + username);
        }

        Authority authority = authorityService.getAuthorityByUserId(user.getId()); // will throw EntityNotFoundException if not found

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
