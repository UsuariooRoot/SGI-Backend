package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterTicketRequest {

    private boolean showNewTickets;
    private List<Integer> statusIds;
    private Integer assignedEmployeeId;
    private Integer ownerEmployeeId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    
}
