package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.service.TicketService;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.infrastructure.api.dto.CustomApiResponse;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.ActionFormRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.CreateTicketFormRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.FilterTicketRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketHistoryResponser;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketResponse;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketResponseMapper;
import com.uoroot.sgi.infrastructure.api.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/tickets")
@CrossOrigin(origins = { "https://tu-dominio-angular.com", "http://localhost:4200" })
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketRequestMapper ticketRequestMapper;
    private final TicketResponseMapper ticketResponseMapper;

    @GetMapping
    public ResponseEntity<CustomApiResponse<List<TicketResponse>>> getTickets(@Valid FilterTicketRequest filter) {
        List<Ticket> tickets = ticketService.getTickets(ticketRequestMapper.toFilterTicket(filter));
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponseList(tickets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<TicketResponse>> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponse(ticket));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<CustomApiResponse<List<TicketHistoryResponser>>> getTicketCurrentHistory(@PathVariable Long id) {
        List<History> histories = ticketService.getTicketHistory(id);
        return ResponseBuilder.success(ticketResponseMapper.toHistoryResponseList(histories));
    }

    @GetMapping("/statuses")
    public ResponseEntity<CustomApiResponse<List<Status>>> getTicketStatuses() {
        List<Status> statuses = ticketService.getStatuses();
        return ResponseBuilder.success(statuses);
    }

    @GetMapping("/requester/{employeeId}")
    public ResponseEntity<CustomApiResponse<List<TicketResponse>>> getTicketsByRequester(@PathVariable Long employeeId,
            FilterTicketRequest filter) {
        Ticket.Filter filters = ticketRequestMapper.toFilterTicket(filter);
        List<Ticket> tickets = ticketService.getTicketsByRequester(filters, employeeId);
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponseList(tickets));
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLEADO_NO_TI')")
    public ResponseEntity<CustomApiResponse<TicketResponse>> createTicket(@RequestBody @Valid CreateTicketFormRequest request) {
        Ticket savedTicket = ticketService.createTicket(request.getIncidentId(), request.getDescription(),
                request.getEmployeeId());
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponse(savedTicket));
    }

    @PostMapping("/action")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLEADO_TI', 'ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<CustomApiResponse<String>> executeAction(@RequestBody @Valid ActionFormRequest request) {
        ticketService.executeAction(request.getEmployeeId(),
            request.getTicketId(),
            request.getActionId(),
            request.getUpdateValue(),
            request.getComment());

        return ResponseBuilder.success("Acción ejecutada exitosamente");
    }
    

}