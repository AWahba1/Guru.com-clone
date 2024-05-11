package com.guru.mediaservice.exceptions;

public class CannotUploadFileToFirebaseException extends RuntimeException {

    public CannotUploadFileToFirebaseException() {
        super("Cannot Upload File to Firebase");
    }
    public CannotUploadFileToFirebaseException(String msg) {
        super(msg);
    }
}
