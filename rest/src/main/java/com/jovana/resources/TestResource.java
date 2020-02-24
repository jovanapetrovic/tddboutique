package com.jovana.resources;

import com.jovana.service.impl.user.RegisterUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by jovana on 24.02.2020
 */
@RestController
@RequestMapping(value = "/api")
public class TestResource {

    @RequestMapping(value = "/testget", method = RequestMethod.GET)
    public ResponseEntity<Void> testGET() {

        System.out.println("===============================");
        System.out.println("This is a public GET request!");
        System.out.println("===============================");

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/testpost", method = RequestMethod.POST)
    public ResponseEntity<Void> testPOST(@Valid @RequestBody RegisterUserRequest registerUserRequest) {

        System.out.println("=================================");
        System.out.println("This is a public POST request!");
        System.out.println(registerUserRequest.getUsername());
        System.out.println(registerUserRequest.getPassword());
        System.out.println("=================================");

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/testauth", method = RequestMethod.GET)
    public ResponseEntity<Void> testAutheticatedGET(HttpServletRequest request) {

        System.out.println("=====================================");
        System.out.println("This is an authenticated GET request!");
        System.out.println("=====================================");

        System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println("Credentials: " + SecurityContextHolder.getContext().getAuthentication().getCredentials());
        System.out.println("Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println("Is user authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        System.out.println("======================================");

        return ResponseEntity.ok().build();
    }

}
