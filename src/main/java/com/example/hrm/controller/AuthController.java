package com.example.hrm.controller;

import com.example.hrm.dto.request.RegisterRequestDTO;
import com.example.hrm.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "Auth endpoint is public";
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return "Register successfully";
    }
}
