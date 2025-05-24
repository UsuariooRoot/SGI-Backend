package com.uoroot.sgi.domain.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    
    public UserNotFoundException(String username) {
        super("Usuario", "username", username);
    }
}
