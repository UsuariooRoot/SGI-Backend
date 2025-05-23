package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.domain.model.Ticket.Filter;;

public interface TicketRepository {

    List<Ticket> findAll(Filter filter);

    List<Ticket> findByEmployeeOwnerId(Filter filter, Long id);

    Ticket findById(Long id);

    History findHistoryById(Long id);

    List<History> findAllHistoryByTicketId(Long id);

    Ticket save(Integer incidentId, String description, Long employeeId);

    void executeAction(Long employeeId,
            Long ticketId,
            Integer actionId,
            Integer updateValue,
            String comment);

}
