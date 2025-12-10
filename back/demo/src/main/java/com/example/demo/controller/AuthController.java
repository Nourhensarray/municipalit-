package com.example.demo.controller;

import com.example.demo.model.ResponsableMunicipal;
import com.example.demo.service.JwtUtil;
import com.example.demo.service.AuthService;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
public Object login(@RequestParam String email, @RequestParam String password) {

    ResponsableMunicipal resp = authService.login(email, password);

    if (resp == null) {
        return Map.of("error", "Invalid credentials"); // JSON plutôt qu’un simple String
    }

    String token = jwtUtil.generateToken(resp.getEmail(), resp.getService());

    // renvoyer token + service
    return Map.of(
        "token", token,
        "service", resp.getService()
    );
}

}
