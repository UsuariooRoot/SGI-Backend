package com.uoroot.sgi.infrastructure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura estándar de respuesta para todas las APIs")
public class CustomApiResponse<T> {

    @Schema(description = "Datos de respuesta, puede ser un objeto o una lista")
    private T data;
    
    @Schema(description = "Contador de elementos cuando la respuesta es una lista", example = "10")
    private int count;

}
