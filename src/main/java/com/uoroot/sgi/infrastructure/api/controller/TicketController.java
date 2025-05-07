package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.service.TicketService;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.infrastructure.api.dto.ApiResponse;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.FilterTicketRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketHistoryResponser;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketResponse;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketResponseMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/tickets")
@CrossOrigin(origins = { "https://tu-dominio-angular.com", "http://localhost:4200" })
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketRequestMapper ticketRequestMapper;
    private final TicketResponseMapper ticketResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getTickets(FilterTicketRequest filter) {
        List<Ticket> tickets = ticketService.getTickets(ticketRequestMapper.toFilterTicket(filter));
        return ResponseEntity.ok(new ApiResponse<>(ticketResponseMapper.toTicketResponseList(tickets), tickets.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketResponseMapper.toTicketResponse(ticket));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<TicketHistoryResponser>>> getTicketCurrentHistory(@PathVariable Long id) {
        List<History> histories = ticketService.getTicketHistory(id);
        return ResponseEntity
                .ok(new ApiResponse<>(ticketResponseMapper.toHistoryResponseList(histories), histories.size()));
    }

    @GetMapping("/statuses")
    public ResponseEntity<ApiResponse<List<Status>>> getMethodName() {
        List<Status> statuses = ticketService.getStatuses();
        return ResponseEntity.ok(new ApiResponse<>(statuses, statuses.size()));
    }

}