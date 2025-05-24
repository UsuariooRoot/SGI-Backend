package com.uoroot.sgi.domain.exception;

public class InvalidTicketActionException extends BusinessLogicException {
    
    public InvalidTicketActionException(String message) {
        super(message);
    }
    
    public InvalidTicketActionException(Long ticketId, Integer actionId) {
        super(String.format("La acción %d no es válida para el ticket con ID: %d", actionId, ticketId));
    }
}
