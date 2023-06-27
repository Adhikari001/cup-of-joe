package com.example.cupofjoe.comms.jwt;

import lombok.Getter;

@Getter
public enum JWTTokenClaimName {
    USER_ID("userId"),
    USERNAME("username"),
    PERMISSION("permission"),
    ROLE("role");

    private final String name;

    JWTTokenClaimName(String name) {
        this.name = name;
    }
}
