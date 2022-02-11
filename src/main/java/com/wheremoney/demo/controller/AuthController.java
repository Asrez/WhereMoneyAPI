package com.wheremoney.demo.controller;

import com.wheremoney.demo.dto.LoginDto;
import com.wheremoney.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
