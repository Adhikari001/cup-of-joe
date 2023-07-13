package com.example.cupofjoe.controller;

import com.example.cupofjoe.comms.jwt.JWTTokenUtil;
import com.example.cupofjoe.constant.enums.OrderStatus;
import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.UpdateResponse;
import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.dto.order.AddOrderRequest;
import com.example.cupofjoe.dto.order.OrderItemRequest;
import com.example.cupofjoe.dto.order.OrderResponse;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    @Autowired
    private OrderController orderController;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MyUserRepository userRepository;

    private final ObjectMapper mapper;
    private final ObjectWriter ow;

    public OrderControllerIntegrationTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void whenOrderIsCreatedOnCompletionOfOrderOrderStatusChangesToComplete() throws Exception{
        MyUser buyer = new MyUser();
        buyer.setFirstName("Buyer first name");
        buyer.setLastName("Buyer last name");
        buyer.setEmail("buyer@example.com");
        buyer.setCafeName("Himalayan Java");
        userRepository.save(buyer);

        MyUser seller = new MyUser();
        seller.setEmail("seller@example.com");
        seller.setFirstName("Seller first name");
        seller.setCafeName("Cafe Swotha");
        userRepository.save(seller);

        List<OrderItemRequest> item = new ArrayList<>();
        item.add(new OrderItemRequest("Coffee", 2, "Low sugar"));
        item.add(new OrderItemRequest("Pastry", 2, "Big slices"));

        String buyerJwt = jwtTokenUtil.createJWT(buyer.getId(), buyer.getEmail(), new String[]{}, "CEO");
        String sellerJwt = jwtTokenUtil.createJWT(seller.getId(), seller.getEmail(), new String[]{}, "CEO");

        //add order request
        final String addOrderUrl = "/api/v1/order/add-order";

        AddOrderRequest addOrderRequest = AddOrderRequest.builder().cafeId(seller.getId()).item(item).build();

        String addOrderRequestJson = ow.writeValueAsString(addOrderRequest);

        MvcResult addOrderResponseMock = mockMvc.perform(post(addOrderUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addOrderRequestJson)
                        .header("Authorization", "Bearer ".concat(buyerJwt)))
                .andExpect(status().isOk()).andReturn();
        Message addOrderMessageResponse = mapper.readValue(addOrderResponseMock.getResponse().getContentAsString(), Message.class);


        final String orderToMeUrl = "/api/v1/order/to-me";
        final String orderByMeUrl = "/api/v1/order/by-me";

        //validating empty sell list of buyer
        mockMvc.perform(get(orderToMeUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addOrderRequestJson)
                        .header("Authorization", "Bearer ".concat(buyerJwt)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));


        //validating empty buy list of seller
        mockMvc.perform(get(orderByMeUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addOrderRequestJson)
                        .header("Authorization", "Bearer ".concat(sellerJwt)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        MvcResult mySalesResponseMock = mockMvc.perform(get(orderToMeUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addOrderRequestJson)
                        .header("Authorization", "Bearer ".concat(sellerJwt)))
                .andExpect(status().isOk())
                .andReturn();


        List<OrderResponse> mySalesResponse = mapper.readValue(mySalesResponseMock.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, OrderResponse.class));

        MvcResult myBuyResponseMock = mockMvc.perform(get(orderByMeUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(addOrderRequestJson)
                        .header("Authorization", "Bearer ".concat(buyerJwt)))
                .andExpect(status().isOk())
                .andReturn();

        List<OrderResponse> myBuyResponse = mapper.readValue(myBuyResponseMock.getResponse().getContentAsString(), mapper.getTypeFactory().constructCollectionType(List.class, OrderResponse.class));

        //validating all ids are same
        OrderResponse myFirstBuy = myBuyResponse.stream().findFirst().get();
        OrderResponse myFirstSell = mySalesResponse.stream().findFirst().get();

        Assertions.assertEquals(addOrderMessageResponse.getId(), myFirstBuy.getOrderId());
        Assertions.assertEquals(addOrderMessageResponse.getId(), myFirstSell.getOrderId());

        //validating order status ORDERED
        Assertions.assertEquals(OrderStatus.ORDERED.name(),myFirstBuy.getOrderStatus());

        final String markAsPreparedUrl = "/api/v1/order/prepared/";
        MvcResult markAsPreparedMock = mockMvc.perform(put(markAsPreparedUrl.concat(String.valueOf(myFirstBuy.getOrderId())))
                        .header("Authorization", "Bearer ".concat(sellerJwt)))
                .andExpect(status().isOk())
                .andReturn();
        UpdateResponse<OrderResponse> markAsPreparedResponse = mapper.readValue(markAsPreparedMock.getResponse().getContentAsString(), mapper.getTypeFactory().constructParametricType(UpdateResponse.class, OrderResponse.class));
        Assertions.assertEquals(OrderStatus.PREPARED.name(), markAsPreparedResponse.getItem().getOrderStatus());


        final String markAsCompleteUrl = "/api/v1/order/completed/";
        MvcResult markAsCompleteMock = mockMvc.perform(put(markAsCompleteUrl.concat(String.valueOf(myFirstBuy.getOrderId())))
                        .header("Authorization", "Bearer ".concat(sellerJwt)))
                .andExpect(status().isOk())
                .andReturn();
        UpdateResponse<OrderResponse> markAsCompeteResponse = mapper.readValue(markAsCompleteMock.getResponse().getContentAsString(), mapper.getTypeFactory().constructParametricType(UpdateResponse.class, OrderResponse.class));
        Assertions.assertEquals(OrderStatus.COMPLETED.name(), markAsCompeteResponse.getItem().getOrderStatus());

    }



}
