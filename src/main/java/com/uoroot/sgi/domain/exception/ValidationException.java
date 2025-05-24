package com.uoroot.sgi.domain.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends BadRequestException {
    
    private final List<String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }
    
    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
}
