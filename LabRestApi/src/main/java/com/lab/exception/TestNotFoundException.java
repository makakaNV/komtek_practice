package com.lab.exception;

public class TestNotFoundException extends RuntimeException{
    public TestNotFoundException(String message) {
        super(message);
    }
}
