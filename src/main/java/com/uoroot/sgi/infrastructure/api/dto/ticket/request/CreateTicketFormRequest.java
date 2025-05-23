package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTicketFormRequest {

    private Long employeeId;
    private Integer incidentId;
    private String description;

}
