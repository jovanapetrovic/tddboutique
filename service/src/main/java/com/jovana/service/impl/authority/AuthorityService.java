package com.jovana.service.impl.authority;

import com.jovana.entity.authority.Authority;

import java.util.List;

/**
 * Created by jovana on 24.02.2020
 */
public interface AuthorityService {

    Authority getAuthorityByUserId(Long userId);

    List<Authority> getAllAuthorities();

}
