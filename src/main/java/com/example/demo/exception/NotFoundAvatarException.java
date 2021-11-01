package com.example.demo.exception;

public class NotFoundAvatarException extends RuntimeException {

    public NotFoundAvatarException(String message) {
        super(message);
    }

    public NotFoundAvatarException() {
    }

}
