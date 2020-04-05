package com.jovana.service.impl.user;

import com.jovana.entity.user.User;
import com.jovana.entity.user.dto.ChangeEmailAddressRequest;
import com.jovana.entity.user.dto.ChangePasswordRequest;
import com.jovana.entity.user.dto.ChangeUsernameRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;

/**
 * Created by jovana on 24.02.2020
 */
public interface UserService {

    User getUserById(Long userId);

    Long registerUser(RegisterUserRequest registerUserRequest);

    void changeEmailAddress(Long userId, ChangeEmailAddressRequest changeEmailAddressRequest);

    void changeUsername(Long userId, ChangeUsernameRequest changeUsernameRequest);

    void changePassword(Long userId, ChangePasswordRequest changePasswordRequest);

}
