package com.pm.patientservice.Exception;

public class EmailAlredayExistsException extends RuntimeException {
    public EmailAlredayExistsException(String message) {
        super(message);
    }
}
