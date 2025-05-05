package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.domain.repository.TicketRepository;
import com.uoroot.sgi.domain.service.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> getTickets(Ticket.Filter filter) {
        return ticketRepository.findAll(filter);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<History> getTicketHistory(Long id) {
        return ticketRepository.findAllHistoryByTicketId(id);
    }

}
