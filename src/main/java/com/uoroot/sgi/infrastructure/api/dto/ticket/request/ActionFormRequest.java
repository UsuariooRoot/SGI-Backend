package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

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
public class ActionFormRequest {
    
    @NotNull(message = "El ID del empleado es requerido")
    private Long employeeId;
    
    @NotNull(message = "El ID del ticket es requerido")
    private Long ticketId;
    
    @NotNull(message = "El ID de la acción es requerido")
    private Integer actionId;
    
    @NotNull(message = "El valor de actualización es requerido")
    private Integer updateValue;
    
    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comment;

}
