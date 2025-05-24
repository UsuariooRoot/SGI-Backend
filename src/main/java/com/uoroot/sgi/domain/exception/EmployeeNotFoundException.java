package com.uoroot.sgi.domain.exception;

public class EmployeeNotFoundException extends ResourceNotFoundException {
    
    public EmployeeNotFoundException(Long id) {
        super("Empleado", "id", id);
    }
    
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
