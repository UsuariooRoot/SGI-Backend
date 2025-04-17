package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Ticket;

public interface TicketRepository {

    List<Ticket> findAll(Ticket.Filter filter);

    Ticket findById(Long id);

    History findHistoryById(Long id);

}
