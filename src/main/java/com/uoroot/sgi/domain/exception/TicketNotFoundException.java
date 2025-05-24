package com.uoroot.sgi.domain.exception;

public class TicketNotFoundException extends ResourceNotFoundException {
    
    public TicketNotFoundException(Long id) {
        super("Ticket", "id", id);
    }
    
    public TicketNotFoundException(String message) {
        super(message);
    }
}
