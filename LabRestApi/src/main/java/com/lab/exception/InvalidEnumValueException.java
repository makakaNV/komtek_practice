package com.lab.exception;

public class InvalidEnumValueException extends RuntimeException{
    private final String fieldName;
    private final String[] validValues;

    public InvalidEnumValueException(String fieldName, String[] validValues) {
        super("Некорректное значение для поля " + fieldName);
        this.fieldName = fieldName;
        this.validValues = validValues;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String[] getValidValues() {
        return validValues;
    }
}
