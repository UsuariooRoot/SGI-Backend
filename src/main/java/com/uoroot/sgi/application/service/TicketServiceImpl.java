package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.domain.model.Ticket.Filter;
import com.uoroot.sgi.domain.repository.StatusRepository;
import com.uoroot.sgi.domain.repository.TicketRepository;
import com.uoroot.sgi.domain.service.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final StatusRepository statusRepository;

    public List<Ticket> getTickets(Filter filter) {
        return ticketRepository.findAll(filter);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<History> getTicketHistory(Long id) {
        return ticketRepository.findAllHistoryByTicketId(id);
    }

    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }

    public List<Ticket> getTicketsByRequester(Filter filter, Long employeeId) {
        return List.of();
    }

}
