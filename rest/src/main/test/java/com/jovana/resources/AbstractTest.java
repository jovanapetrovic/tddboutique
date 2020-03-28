package com.jovana.resources;

import com.jovana.config.RestConfig;
import com.jovana.util.ObjectToJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by jovana on 28.03.2020
 */
@WebMvcTest(value = RegisterResource.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@SpringJUnitWebConfig(RestConfig.class)
public class AbstractTest {

    public static String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

    public HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    public CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    public ObjectToJsonMapper objectToJsonMapper = new ObjectToJsonMapper();

    @Autowired
    public MockMvc mockMvc;

}
