package com.jovana.service.impl.authority;

import com.jovana.entity.EntityNotFoundException;
import com.jovana.entity.authority.Authority;
import com.jovana.repositories.authority.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jovana on 24.02.2020
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityByUserId(Long userId) {
        Authority authority = authorityRepository.findByUserId(userId);

        if (authority == null) {
            LOGGER.info("Authority for user with id = {} was not found in the db.", userId);
            throw new EntityNotFoundException("No authority found for user with id = " + userId);
        }

        return authorityRepository.findByUserId(userId);
    }

    @Override
    public List<Authority> getAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();

        if (!authorities.isEmpty()) {
            return authorities;
        } else {
            throw new EntityNotFoundException("No authorities found in the db.");
        }
    }
}
