package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos para ejecutar una acción sobre un ticket")
public class ActionFormRequest {
    
    @NotNull(message = "El ID del empleado es requerido")
    @Schema(description = "ID del empleado que ejecuta la acción", example = "1", required = true)
    private Long employeeId;
    
    @NotNull(message = "El ID del ticket es requerido")
    @Schema(description = "ID del ticket sobre el que se ejecuta la acción", example = "42", required = true)
    private Long ticketId;
    
    @NotNull(message = "El ID de la acción es requerido")
    @Schema(description = "ID de la acción a ejecutar (asignar, resolver, cerrar, etc.)", example = "2", required = true)
    private Integer actionId;
    
    @NotNull(message = "El valor de actualización es requerido")
    @Schema(description = "Valor asociado a la acción (ID de estado, ID de empleado asignado, etc.)", example = "3", required = true)
    private Integer updateValue;
    
    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    @Schema(description = "Comentario adicional sobre la acción realizada", example = "Se asigna el ticket al equipo de soporte de infraestructura")
    private String comment;

}
