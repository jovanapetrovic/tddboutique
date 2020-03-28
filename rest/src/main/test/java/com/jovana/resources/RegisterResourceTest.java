package com.jovana.resources;

import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.service.impl.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by jovana on 28.03.2020
 */
public class RegisterResourceTest extends AbstractTest {

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterUserSuccess() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                "John",
                "Doe",
                "johndoe@test.com",
                "johndoe",
                "123456",
                "123456");

        mockMvc.perform(post("/api/register/demo", registerUserRequest)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .content(objectToJsonMapper.asJsonString(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testRegisterUserPOSTFailsWhenFirstNameIsNull() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                null,
                "Doe",
                "johndoe@test.com",
                "johndoe",
                "123456",
                "123456");

        mockMvc.perform(post("/api/register/demo", registerUserRequest)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .content(objectToJsonMapper.asJsonString(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\"validationErrors\":[{\"fieldName\":\"firstName\",\"message\":\"must not be null\"}]}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
