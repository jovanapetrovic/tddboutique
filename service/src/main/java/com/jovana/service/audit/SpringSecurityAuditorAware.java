package com.jovana.service.audit;

import com.jovana.service.Constants;
import com.jovana.service.security.SecurityUtils;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by jovana on 26.03.2020
 *
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = SecurityUtils.getCurrentUsername();
        return Optional.of(username != null ? username : Constants.SYSTEM_ACCOUNT);
    }

}
