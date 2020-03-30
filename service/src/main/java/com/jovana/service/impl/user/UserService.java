package com.jovana.service.impl.user;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.RegisterUserRequest;

/**
 * Created by jovana on 24.02.2020
 */
public interface UserService {

    User getUserById(Long userId);

    Long registerUser(RegisterUserRequest registerUserRequest);

}
