package com.ohseoul.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordComparisonService {

    private final PasswordEncoder passwordEncoder;

    public PasswordComparisonService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}