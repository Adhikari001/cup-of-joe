package com.example.cupofjoe.dto.cafe;

import lombok.Data;

@Data
public class CafeResponse {
    private String cafeId;
    private String cafeName;

    public CafeResponse(String cafeId, String cafeName) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
    }

    public CafeResponse(){}
}
