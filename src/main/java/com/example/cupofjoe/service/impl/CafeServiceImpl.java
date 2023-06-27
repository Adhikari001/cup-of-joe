package com.example.cupofjoe.service.impl;

import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.service.CafeService;
import com.example.cupofjoe.service.MyUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafeServiceImpl implements CafeService {

    private final MyUserService myUserService;

    public CafeServiceImpl(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @Override
    public List<CafeResponse> getCafeList() {
        return myUserService.getCafeList();
    }
}
