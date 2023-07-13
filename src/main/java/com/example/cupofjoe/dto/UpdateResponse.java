package com.example.cupofjoe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class UpdateResponse<T> {
    private T item;
    private String message;

    public UpdateResponse(T item, String message) {
        this.item = item;
        this.message = message;
    }
}
