package com.example.cupofjoe.service;

import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.dto.myuser.*;

import java.util.List;

public interface MyUserService {
    RegistrationResponse registrationRequest(RegistrationRequest request);

    ValidateOtpResponse validateOtpRequest(ValidateOtp request);

    AddInformationResponse addInformationRequest(AddInformationRequest request);

    LoginResponse loginRequest(LoginRequest request);

    Message logout();

    List<CafeResponse> getCafeList();
}
