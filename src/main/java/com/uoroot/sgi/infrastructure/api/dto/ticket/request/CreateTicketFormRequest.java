package com.uoroot.sgi.infrastructure.api.dto.ticket.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos para crear un nuevo ticket de incidente")
public class CreateTicketFormRequest {

    @NotNull(message = "El ID del empleado es requerido")
    @Schema(description = "ID del empleado que solicita el ticket", example = "1", required = true)
    private Long employeeId;
    
    @NotNull(message = "El ID del incidente es requerido")
    @Schema(description = "ID del tipo de incidente reportado", example = "2", required = true)
    private Integer incidentId;
    
    @Schema(description = "Descripción detallada del incidente reportado", example = "La impresora del piso 3 no funciona correctamente")
    private String description;

}
