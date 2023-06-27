package com.example.cupofjoe.comms.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@AllArgsConstructor
@Data
public class ClaimResponse {
    private String userId;
    private String username;
    private ArrayList<String> permission;
    private String role;
}
