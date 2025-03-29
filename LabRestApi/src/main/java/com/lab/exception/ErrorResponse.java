package com.lab.exception;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {

    @Schema(description = "Сообщение об ошибке", example = "Пациентов с id-404 не найдено")
    private String message;

    @Schema(description = "Код ошибки", example = "404")
    private int statusCode;

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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}