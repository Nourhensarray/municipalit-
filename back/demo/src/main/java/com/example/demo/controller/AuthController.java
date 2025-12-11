package com.example.demo.controller;

import com.example.demo.model.ResponsableMunicipal;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Object login(@RequestParam String email, @RequestParam String password) {

        ResponsableMunicipal resp = authService.login(email, password);

        if (resp == null) {
            return Map.of("error", "Invalid credentials"); // JSON plutôt qu’un simple String
        }

        String token = jwtService.generateToken(resp.getEmail(), resp.getService());

        // renvoyer token + service
        return Map.of(
            "token", token,
            "service", resp.getService()
        );
    }
}
