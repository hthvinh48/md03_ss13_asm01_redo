package com.example.hrm.controller;

import com.example.hrm.dto.request.LoginRequest;
import com.example.hrm.dto.request.RegisterRequestDTO;
import com.example.hrm.security.AuthService;
import com.example.hrm.security.UserPrincipal;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public String test() {
        return "Auth endpoint is public";
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return "Register successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequestDTO,
            HttpSession session
    ) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDTO.getUsername(),
                                    loginRequestDTO.getPassword()
                            )
                    );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            UserPrincipal principal =
                    (UserPrincipal) authentication.getPrincipal();

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successfully",
                            "username", principal.getUsername()
                    )
            );

        } catch (BadCredentialsException exception) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("username or password incorrect");
        }
    }
}
