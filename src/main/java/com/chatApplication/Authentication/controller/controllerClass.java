package com.chatApplication.Authentication.controller;


import com.chatApplication.Authentication.exceptions.ApiResponse;
import com.chatApplication.Authentication.model.Users;
import com.chatApplication.Authentication.service.userService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class controllerClass {

    @Autowired
    private userService service;

    private static final Logger logger= LoggerFactory.getLogger(controllerClass.class);

    @GetMapping("/retrive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Users> retriveAll(){
        logger.info("into retrive ###################################");
        return service.retrive();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registration(@RequestBody Users users) {
        return service.register(users);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Users users) {
        return service.verify(users);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("jwtToken");
        if (token == null) {
            return ResponseEntity.badRequest().body("Invalid request. Token not found.");
        }

        service.logout(token);

        return ResponseEntity.ok("User logged out successfully");
    }





}
