package com.example.cupofjoe.controller;

import com.example.cupofjoe.constant.route.Routes;
import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.service.CafeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CafeController {
    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping(Routes.CAFE)
    public List<CafeResponse> getCafeName(){
        log.info("get cafe name");
        return cafeService.getCafeList();
    }
}
