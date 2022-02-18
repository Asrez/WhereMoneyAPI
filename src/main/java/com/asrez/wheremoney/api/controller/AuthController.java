package com.asrez.wheremoney.api.controller;

import com.asrez.wheremoney.api.dto.LoginDto;
import com.asrez.wheremoney.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity signIn(@RequestBody LoginDto loginDto) {
        return ok(authService.signIn(loginDto.getUsername(), loginDto.getPassword()));
    }

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ok(authService.currentUser(userDetails));
    }
}
