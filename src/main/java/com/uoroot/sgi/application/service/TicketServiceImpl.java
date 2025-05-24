package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.exception.BadRequestException;
import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.EmployeeNotFoundException;
import com.uoroot.sgi.domain.exception.InvalidTicketActionException;
import com.uoroot.sgi.domain.exception.TicketNotFoundException;
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
        List<Ticket> tickets = ticketRepository.findAll(filter);
        if (tickets == null) {
            throw new BusinessLogicException("Error al obtener los tickets");
        }
        return tickets;
    }

    public Ticket getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            throw new TicketNotFoundException(id);
        }
        return ticket;
    }

    public List<History> getTicketHistory(Long id) {
        Ticket ticket = ticketRepository.findById(id);

        // Check if the ticket exists
        if (ticket == null) {
            throw new TicketNotFoundException(id);
        }

        List<History> history = ticketRepository.findAllHistoryByTicketId(id);
        if (history == null) {
            throw new BusinessLogicException("Error al obtener el historial del ticket");
        }

        return history;
    }

    public List<Status> getStatuses() {
        List<Status> statuses = statusRepository.findAll();
        if (statuses == null) {
            throw new BusinessLogicException("Error al obtener los estados");
        }
        return statuses;
    }

    public List<Ticket> getTicketsByRequester(Filter filter, Long employeeId) {
        if (employeeId == null) {
            throw new BadRequestException("El ID del empleado no puede ser nulo");
        }

        List<Ticket> tickets = ticketRepository.findByEmployeeOwnerId(filter, employeeId);
        if (tickets == null) {
            throw new BusinessLogicException("Error al obtener los tickets del empleado");
        }
        return tickets;
    }

    public Ticket createTicket(Integer incidentId, String description, Long employeeId) {
        Long savedTicketId = ticketRepository.save(incidentId, description, employeeId);
        if (savedTicketId == null) {
            throw new BusinessLogicException("Error al crear el ticket");
        }
        return ticketRepository.findById(savedTicketId);
    }

    public void executeAction(Long employeeId, Long ticketId, Integer actionId, Integer updateValue, String comment) {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException(ticketId);
        }

        try {
            ticketRepository.executeAction(
                    employeeId,
                    ticketId,
                    actionId,
                    updateValue,
                    comment);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("acción no válida")) {
                throw new InvalidTicketActionException(ticketId, actionId);
            } else if (e.getMessage() != null && e.getMessage().contains("empleado no encontrado")) {
                throw new EmployeeNotFoundException(employeeId);
            } else {
                throw new BusinessLogicException("Error al ejecutar la acción: " + e.getMessage());
            }
        }
    }

}
