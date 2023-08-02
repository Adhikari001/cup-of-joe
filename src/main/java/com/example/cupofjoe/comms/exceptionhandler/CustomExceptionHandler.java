package com.example.cupofjoe.comms.exceptionhandler;

import com.example.cupofjoe.comms.helper.HelperUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, String>> handelRestException(RestException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", ex.getCode());
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Request body not valid.");
        errorResponse.put("code", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("timeStamp", HelperUtil.getLocalDateTimeOfUTC());
        errorResponse.put("detailMessage", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }



}
