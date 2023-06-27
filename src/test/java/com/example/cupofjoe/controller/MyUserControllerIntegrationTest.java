package com.example.cupofjoe.controller;

import com.example.cupofjoe.dto.myuser.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class MyUserControllerIntegrationTest {


    MockMvc mockMvc;
    ObjectMapper mapper;
    ObjectWriter ow;

    @Autowired
    public MyUserControllerIntegrationTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void firstTest() throws Exception{
        this.mockMvc.perform(get("/")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InNhdXJhdiIsInBlcm1pc3Npb24iOlsiQ0VPIiwiRGV2ZWxvcGVyIl0sInJvbGUiOiJBRE1JTiIsImp0aSI6ImhlbGxvIiwiaWF0IjoxNjg3ODQ2MjQ3LCJleHAiOjE2ODc5MzI2NDd9.fp5gXwFzUQmHPqGIoZkGKJc-8u2jJSX9ZaUSLmcDY8VcJu26E5GBfJyiyyVLHwY8MN45H5Cgl4ZMgss1ttij2Q")
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }

    @Test
    public void givenUser_onRegistrationProcess_CanLoginSuccessfully() throws Exception{

        //register request
        String registerUrl = "/api/v1/user/register";
        RegistrationRequest registerRequest = RegistrationRequest.builder().email("sauravadhikari001@gmail.com").build();

        String registerRequestJson = ow.writeValueAsString(registerRequest);

        MvcResult registerMvcResult = mockMvc.perform(post(registerUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isOk()).andReturn();

        //validate otp request
        String registerResponseString = registerMvcResult.getResponse().getContentAsString();

        RegistrationResponse registrationResponse = mapper.readValue(registerResponseString, RegistrationResponse.class);

        String validateOTPUrl = "/api/v1/user/validate-otp";
        ValidateOtp validateOtp = ValidateOtp.builder().userId(registrationResponse.getId())
                .otp(registrationResponse.getOtp())
                .build();

        String validateOtpRequest =ow.writeValueAsString(validateOtp);

        MvcResult validateOtpMvcResult= mockMvc.perform(post(validateOTPUrl).contentType(MediaType.APPLICATION_JSON)
                .content(validateOtpRequest))
                .andExpect(status().isOk()).andReturn();

        //add information request
        String validateOtpResponseString = validateOtpMvcResult.getResponse().getContentAsString();

        ValidateOtpResponse validateOtpResponse = mapper.readValue(validateOtpResponseString, ValidateOtpResponse.class);

        String jwt = validateOtpResponse.getToken();

        String addInformationUrl = "/api/v1/user/add-information";
        AddInformationRequest addInformation = AddInformationRequest.builder().firstName("Saurav")
                .lastName("Adhikari")
                .password("Saurav@123")
                .conformPassword("Saurav@123")
                .cafeName("Himalyan Java")
                .build();

        String addInformationRequest = ow.writeValueAsString(addInformation);

        MvcResult addInformationMvcResult = mockMvc.perform(post(addInformationUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addInformationRequest)
                        .header("Authorization", "Bearer ".concat(jwt)))
                .andExpect(status().isOk())
                .andReturn();

        //login request
        String loginUrl = "/api/v1/user/login";
        LoginRequest loginRequest = LoginRequest.builder().email("sauravadhikari001@gmail.com").password("Saurav@123").build();

        String loginRequestString = ow.writeValueAsString(loginRequest);

        MvcResult loginMvcResult = mockMvc.perform(post(loginUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestString))
                .andExpect(status().isOk()).andReturn();



    }


}
