package com.example.cupofjoe.comms.exceptionhandler;

import com.example.cupofjoe.comms.helper.HelperUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestException> handelRestException(RestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Request body not valid.");
        errorResponse.put("code", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("timeStamp", HelperUtil.getLocalDateTimeOfUTC());
        errorResponse.put("detailMessage", "");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }



}
