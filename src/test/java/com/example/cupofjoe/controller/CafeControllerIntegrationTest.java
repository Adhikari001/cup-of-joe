package com.example.cupofjoe.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cupofjoe.comms.jwt.JWTTokenUtil;
import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.dto.myuser.ValidateOtpResponse;
import com.example.cupofjoe.entity.MyUser;
import com.example.cupofjoe.repository.MyUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CafeControllerIntegrationTest {
    @Autowired
    private CafeController cafeController;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MyUserRepository userRepository;

    private ObjectMapper mapper;

    private final ObjectWriter ow;


    public CafeControllerIntegrationTest(){
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }


    @Test
    public void whenCafeIsPresent_OnCafeRequest_getsAllPresentCafe() throws Exception{

        MyUser myUserOne = new MyUser();
        myUserOne.setCafeName("Himalayan Java");
        userRepository.save(myUserOne);

        MyUser myUserTwo = new MyUser();
        myUserTwo = userRepository.save(myUserTwo);

        MyUser myUserThree = new MyUser();
        myUserThree.setCafeName("Cafe Swotha");
        myUserThree = userRepository.save(myUserThree);

        List<CafeResponse> expectedCafeResponses = new ArrayList<>();
        expectedCafeResponses.add(new CafeResponse(myUserOne.getId(), myUserOne.getCafeName()));
        expectedCafeResponses.add(new CafeResponse(myUserThree.getId(), myUserThree.getCafeName()));


        String jwt = jwtTokenUtil.createJWT("test-user-id", "saurav@gmail.com", new String[]{}, "CEO");

        String cafeListUrl = "/api/v1/cafe";

        MvcResult getCafeResponse = mockMvc.perform(get(cafeListUrl)
                        .header("Authorization", "Bearer ".concat(jwt)))
                .andExpect(status().isOk())
                .andReturn();
        List<CafeResponse> actualCafeResponses = mapper.readValue(getCafeResponse.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, CafeResponse.class));

        Assertions.assertEquals(expectedCafeResponses, actualCafeResponses);
    }
}
