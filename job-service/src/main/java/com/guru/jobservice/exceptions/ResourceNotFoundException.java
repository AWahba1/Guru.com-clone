package com.guru.jobservice.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("No resource found matches the given ID");
    }
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
