package com.uoroot.sgi.domain.exception;

public class UsernameAlreadyExistsException extends BadRequestException {
    
    public UsernameAlreadyExistsException(String username) {
        super("El nombre de usuario '" + username + "' ya está en uso");
    }
}
