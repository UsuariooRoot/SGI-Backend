package com.uoroot.sgi.infrastructure.api.dto.incident.request;

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
public class IncidentRequest {
    @NotBlank(message = "La descripción es requerida")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String description;
    
    @NotNull(message = "El ID de la categoría es requerido")
    private Integer categoryId;
    
    @NotNull(message = "El ID de la prioridad es requerido")
    private Integer priorityId;
}
