package com.uoroot.sgi.domain.exception;

public class InvalidTicketStateException extends BusinessLogicException {
    
    public InvalidTicketStateException(String message) {
        super(message);
    }
    
    public InvalidTicketStateException(Long ticketId, String currentState, String requiredState) {
        super(String.format("El ticket con ID %d está en estado '%s', pero se requiere estado '%s' para esta operación", 
                ticketId, currentState, requiredState));
    }
}
