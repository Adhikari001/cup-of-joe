package com.example.cupofjoe.comms.exceptionhandler;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {
    private final String code;
    private final String message;

    public RestException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
