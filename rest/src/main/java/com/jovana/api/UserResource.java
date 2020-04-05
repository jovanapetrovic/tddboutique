package com.jovana.api;

import com.jovana.entity.PathConstants;
import com.jovana.entity.user.dto.ChangeEmailAddressRequest;
import com.jovana.entity.user.dto.ChangePasswordRequest;
import com.jovana.entity.user.dto.ChangeUsernameRequest;
import com.jovana.entity.user.dto.RegisterUserRequest;
import com.jovana.service.impl.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by jovana on 24.02.2020
 */
@RestController
@RequestMapping(value = PathConstants.API)
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping(value = PathConstants.REGISTER)
    public ResponseEntity<Void> registerUserPOST(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        userService.registerUser(registerUserRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = PathConstants.USER_CHANGE_EMAIL_ADDRESS)
    public ResponseEntity<Void> changeEmailAddressPUT(@PathVariable("userId") Long userId,
                                                      @Valid @RequestBody ChangeEmailAddressRequest changeEmailAddressRequest) {
        userService.changeEmailAddress(userId, changeEmailAddressRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = PathConstants.USER_CHANGE_USERNAME)
    public ResponseEntity<Void> changeUsernamePUT(@PathVariable("userId") Long userId,
                                                  @Valid @RequestBody ChangeUsernameRequest changeUsernameRequest) {
        userService.changeUsername(userId, changeUsernameRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = PathConstants.USER_CHANGE_PASSWORD)
    public ResponseEntity<Void> changePasswordPUT(@PathVariable("userId") Long userId,
                                                  @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

}
