package com.example.cupofjoe.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateResponse<T> {
    private T item;
    private String message;

    public UpdateResponse(T item, String message) {
        this.item = item;
        this.message = message;
    }
}
