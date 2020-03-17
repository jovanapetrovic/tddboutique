package com.jovana.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by jovana on 24.02.2020
 */
@RestController
@RequestMapping(value = "/api/register")
public class RegisterResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterResource.class);

//    @Autowired
//    private UserService userService;
//
//    @RequestMapping(value = "/demo", method = RequestMethod.POST)
//    public ResponseEntity<Void> registerUserPOST(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
//
//        userService.registerUser(registerUserRequest); // will throw an error if username already exists
//
//        return ResponseEntity.ok().build();
//    }

}
