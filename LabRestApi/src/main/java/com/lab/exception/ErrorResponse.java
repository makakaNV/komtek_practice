package com.lab.exception;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {

    @Schema(description = "Сообщение об ошибке", example = "Пациентов с id-404 не найдено")
    private String message;

    @Schema(description = "Код ошибки", example = "404")
    private int statusCode;

    @SuppressWarnings("unused")
    public ErrorResponse() {}

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public int getStatusCode() {
        return statusCode;
    }

    @SuppressWarnings("unused")
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}