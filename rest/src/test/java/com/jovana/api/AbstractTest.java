package com.jovana.api;

import com.jovana.config.RestConfig;
import com.jovana.util.FileUtil;
import com.jovana.util.ObjectToJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by jovana on 28.03.2020
 */
@WebMvcTest(controllers = {UserResource.class, ShippingAddressResource.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@SpringJUnitWebConfig(RestConfig.class)
public class AbstractTest {

    public static final String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

    public HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    public CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    @Autowired
    public MockMvc mockMvc;

    public void performSimpleGet(String path, String jsonFileName) throws Exception {
        mockMvc.perform(get(path)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(FileUtil.readFileAndConvertItToString(jsonFileName)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public void performSimplePost(String path, Object requestBody, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(post(path, requestBody)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .content(ObjectToJsonMapper.asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(expectedStatus);
    }

    public void performPostAndExpectResponse(String path, Object requestBody, String jsonFileName, ResultMatcher expectedStatus) throws Exception {
        mockMvc.perform(post(path, requestBody)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .content(ObjectToJsonMapper.asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(FileUtil.readFileAndConvertItToString(jsonFileName)))
                .andExpect(expectedStatus);
    }

}
