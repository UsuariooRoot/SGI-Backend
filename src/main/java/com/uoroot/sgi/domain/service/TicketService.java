package com.uoroot.sgi.domain.service;

import java.util.List;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Ticket;

public interface TicketService {

    List<Ticket> getTickets(Ticket.Filter filter);

    Ticket getTicketById(Long id);

    List<History> getTicketHistory(Long id);

}
