package com.example.cupofjoe.constant.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    ;
    private final String code;
    private final String message;

    ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorMessage(String code) {
        this.code = code;
        this.message = null;
    }
}
