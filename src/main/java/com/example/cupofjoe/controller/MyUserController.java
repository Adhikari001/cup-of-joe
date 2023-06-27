package com.example.cupofjoe.controller;

import com.example.cupofjoe.constant.route.Routes;
import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.myuser.*;
import com.example.cupofjoe.service.MyUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class MyUserController {
    private final MyUserService myUserService;

    @Autowired
    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/")
    public String helloTest(){
        return "Hello, World";
    }

    @PostMapping(Routes.REGISTER)
    public RegistrationResponse registrationRequest(@Valid @RequestBody RegistrationRequest request){
        log.info("Registration request {}", request);
        return myUserService.registrationRequest(request);
    }

    @PostMapping(Routes.VALIDATE_OTP)
    public ValidateOtpResponse validateOTP(@Valid @RequestBody ValidateOtp request){
        log.info("Validate otp request {}", request);
        return myUserService.validateOtpRequest(request);
    }

    @PostMapping(Routes.ADD_INFORMATION)
    public AddInformationResponse addInformation(@Valid @RequestBody AddInformationRequest request){
        log.info("Add information request {}", request);
        return myUserService.addInformationRequest(request);
    }

    @PostMapping(Routes.LOGIN)
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        log.info("Login request {}", request);
        return myUserService.loginRequest(request);
    }

    @PostMapping(Routes.LOGOUT)
    public Message logoutRequest(){
        log.info("Logout request");
        return myUserService.logout();
    }

}
