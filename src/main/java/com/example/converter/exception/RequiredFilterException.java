package com.example.converter.exception;

public class RequiredFilterException extends Exception {
    public RequiredFilterException(String filterOne, String filterTwo) {
        super(("At least one of these fields must be provided: " + filterOne + " and/or " + filterTwo));
    }
}
