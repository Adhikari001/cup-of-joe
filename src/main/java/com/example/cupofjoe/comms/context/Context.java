package com.example.cupofjoe.comms.context;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Context {
    private final String userId;
    private final String username;
    private final String role;
    private final List<String> permission;

    public Context(String userId, String username, String role, List<String> permission) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.permission = permission;
    }
}
