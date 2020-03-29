package com.jovana.api;

import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.service.impl.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Created by jovana on 28.03.2020
 */
public class RegisterResourceTest extends AbstractTest {

    private static final String REGISTER_API_PATH = "/api/register/demo";
    private static final String REGISTER_USER_RESPONSE_WHEN_PARAMS_ARE_NULL = "registerUserResponseWhenParamsAreNull.json";
    private static final String REGISTER_USER_RESPONSE_WHEN_PARAMS_SIZE_IS_WRONG = "registerUserResponseWhenParamsSizeIsWrong.json";
    private static final String REGISTER_USER_RESPONSE_WHEN_EMAIL_HAS_WRONG_FORMAT = "registerUserResponseWhenEmailHasWrongFormat.json";

    @MockBean
    private UserService userService;

    @DisplayName("Register user successfully when request is valid")
    @Test
    public void testRegisterUserSuccess() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                "John",
                "Doe",
                "johndoe@test.com",
                "johndoe",
                "123456",
                "123456");

        performSimplePost(
                REGISTER_API_PATH,
                registerUserRequest,
                MockMvcResultMatchers.status().isCreated());
    }

    @DisplayName("Register user fails when request params are null")
    @Test
    public void testRegisterUserFailsWhenParamsAreNull() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                null,
                null,
                null,
                null,
                null,
                null);

        performPostAndExpectResponse(
                REGISTER_API_PATH,
                registerUserRequest,
                REGISTER_USER_RESPONSE_WHEN_PARAMS_ARE_NULL,
                MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Register user fails when request params size is wrong")
    @Test
    public void testRegisterUserFailsWhenParamsSizeIsWrong() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                "J",
                "D",
                "j@t.c",
                "jd",
                "123",
                "123");

        performPostAndExpectResponse(
                REGISTER_API_PATH,
                registerUserRequest,
                REGISTER_USER_RESPONSE_WHEN_PARAMS_SIZE_IS_WRONG,
                MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Register user fails when email param has wrong format")
    @Test
    public void testRegisterUserFailsWhenEmailHasWrongFormat() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.createRegisterUserRequest(
                "John",
                "Doe",
                "johndoe@",
                "johndoe",
                "123456",
                "123456");

        performPostAndExpectResponse(
                REGISTER_API_PATH,
                registerUserRequest,
                REGISTER_USER_RESPONSE_WHEN_EMAIL_HAS_WRONG_FORMAT,
                MockMvcResultMatchers.status().isBadRequest());
    }

}
