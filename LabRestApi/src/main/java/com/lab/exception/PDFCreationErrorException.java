package com.lab.exception;

public class PDFCreationErrorException extends RuntimeException{
    public PDFCreationErrorException (String message) {
        super(message);
    }
}
