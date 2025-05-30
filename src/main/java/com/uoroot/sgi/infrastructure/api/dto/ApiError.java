package com.uoroot.sgi.infrastructure.api.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estructura estándar para respuestas de error")
public class ApiError {
    @Schema(description = "Código de estado HTTP del error", example = "400")
    private int status;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Datos de entrada inválidos")
    private String message;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora en que ocurrió el error", example = "2025-05-29 20:30:00")
    private LocalDateTime timestamp;
    
    @Builder.Default
    @Schema(description = "Lista de errores específicos", example = "[\"El nombre de usuario es requerido\", \"La contraseña debe tener al menos 6 caracteres\"]")
    private List<String> errors = new ArrayList<>();
    
    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
}
