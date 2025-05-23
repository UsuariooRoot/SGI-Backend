package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionFormRequest {
    
    private Long employeeId;
    private Long ticketId;
    private Integer actionId;
    private Integer updateValue;
    private String comment;

}
