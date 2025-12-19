package com.hustleborn.service.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hustleborn.service.model.users.Users;
import com.hustleborn.service.service.auth.AuthService;
import com.hustleborn.service.util.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Users loginRequest) {
        ApiResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Users registerRequest) {
        ApiResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}