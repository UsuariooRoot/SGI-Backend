package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;

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
    private Integer itTeamId;
    
    @AssertTrue(message = "La fecha de inicio debe ser anterior a la fecha de fin")
    private boolean isDateRangeValid() {
        if (dateFrom == null || dateTo == null) {
            return true;
        }
        // dateFrom must be less than or equal to dateTo
        return !dateFrom.isAfter(dateTo);
    }

}
