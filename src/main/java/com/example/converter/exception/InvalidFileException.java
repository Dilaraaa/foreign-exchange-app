package com.example.converter.exception;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String expectedFileType) {
        super("Invalid file. Please upload a non-empty " + expectedFileType + " file.");
    }
}
