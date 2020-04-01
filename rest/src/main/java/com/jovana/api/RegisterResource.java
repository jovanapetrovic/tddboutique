package com.jovana.api;

import com.jovana.entity.PathConstants;
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
public class RegisterResource {

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

}
