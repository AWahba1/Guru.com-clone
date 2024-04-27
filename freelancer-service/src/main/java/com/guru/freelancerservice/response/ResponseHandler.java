package com.guru.freelancerservice.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj,
                                                          int count) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("message", message);
        map.put("status code", status.value());
        map.put("count", count);
        map.put("data", responseObj);


        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateErrorResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        return new ResponseEntity<Object>(map, status);
    }
}