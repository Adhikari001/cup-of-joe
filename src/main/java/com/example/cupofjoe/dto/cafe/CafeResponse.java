package com.example.cupofjoe.dto.cafe;

import lombok.Data;

@Data
public class CafeResponse {
    private Long cafeId;
    private String cafeName;

    public CafeResponse(Long cafeId, String cafeName) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
    }
}
