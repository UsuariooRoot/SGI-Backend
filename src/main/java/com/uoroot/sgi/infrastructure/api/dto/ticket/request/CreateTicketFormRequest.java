package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    
    @NotBlank(message = "La descripción es requerida")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String description;

}
