package com.pm.patientservice.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {errors.put(error.getField(), error.getDefaultMessage());});
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @ExceptionHandler(EmailAlredayExistsException.class)
    public ResponseEntity<Map<String,String>> handelEmailAlredayExistsException(EmailAlredayExistsException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Message", "Email Already Exists");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String,String>> handelPatinetNotFoundException(PatientNotFoundException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Message", "Patinet Not Found");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
