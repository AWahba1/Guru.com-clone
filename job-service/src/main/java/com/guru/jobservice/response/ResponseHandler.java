package com.guru.jobservice.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public class ResponseHandler {
    public static ResponseEntity<Object> generateGetResponse(String message, HttpStatus status, Object responseObj,
                                                             int count) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("message", message);
        map.put("status code", status.value());
        map.put("count", count);
        map.put("data", responseObj);


        return new ResponseEntity<Object>(map, status);
    }
    public static ResponseEntity<Object> generateGeneralResponse(String message, HttpStatus status) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("message", message);
        map.put("status code", status.value());



        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateErrorResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        return new ResponseEntity<Object>(map, status);
    }
}