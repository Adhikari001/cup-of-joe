package com.example.cupofjoe.comms.passwordencoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.cert.Extension;

@Component
public class MyPasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    public MyPasswordEncoder() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public PasswordEncoder getEncoder() {
        return passwordEncoder;
    }
}
