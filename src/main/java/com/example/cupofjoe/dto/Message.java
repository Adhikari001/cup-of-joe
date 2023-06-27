package com.example.cupofjoe.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Message {
    private final String message;
    private final Long id;
}
