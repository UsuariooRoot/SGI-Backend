package com.uoroot.sgi.domain.service;

import java.util.List;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;

public interface TicketService {

    List<Ticket> getTickets(Ticket.Filter filter);

    Ticket getTicketById(Long id);

    List<History> getTicketHistory(Long id);

    List<Status> getStatuses();

    List<Ticket> getTicketsByRequester(Ticket.Filter filter, Long employeeId);

    Ticket createTicket(Integer incidentId, String description, Long employeeId);

    void executeAction(Long employeeId,
            Long ticketId,
            Integer actionId,
            Integer updateValue,
            String comment);

}
