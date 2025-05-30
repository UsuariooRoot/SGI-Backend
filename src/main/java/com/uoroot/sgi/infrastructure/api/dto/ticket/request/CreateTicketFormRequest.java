package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTicketFormRequest {

    @NotNull(message = "El ID del empleado es requerido")
    private Long employeeId;
    
    @NotNull(message = "El ID del incidente es requerido")
    private Integer incidentId;
    
    private String description;

}
