package com.uoroot.sgi.infrastructure.api.mapper.ticket;

import org.mapstruct.Mapper;

import com.uoroot.sgi.domain.model.FilterTicket;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.FilterTicketRequest;

@Mapper
public interface TicketRequestMapper {
    
    FilterTicket toFilterTicket(FilterTicketRequest request);
    
}
