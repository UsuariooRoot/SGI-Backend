package com.uoroot.sgi.infrastructure.api.mapper.ticket;

import org.mapstruct.Mapper;

import com.uoroot.sgi.domain.model.Ticket.Filter;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.FilterTicketRequest;

@Mapper
public interface TicketRequestMapper {
    
    Filter toFilterTicket(FilterTicketRequest request);
    
}
