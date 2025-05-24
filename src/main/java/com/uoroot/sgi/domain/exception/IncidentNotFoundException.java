package com.uoroot.sgi.domain.exception;

public class IncidentNotFoundException extends ResourceNotFoundException {
    
    public IncidentNotFoundException(Integer id) {
        super("Incident", "id", id);
    }
    
    public IncidentNotFoundException(String message) {
        super(message);
    }
}
