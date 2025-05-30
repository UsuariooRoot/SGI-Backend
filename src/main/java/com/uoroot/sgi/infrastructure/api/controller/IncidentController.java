package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.ResourceNotFoundException;
import com.uoroot.sgi.domain.service.IncidentCategoryService;
import com.uoroot.sgi.domain.service.IncidentService;
import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.infrastructure.api.dto.incident.request.IncidentRequest;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentCategoryResponseMapper;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentResponseMapper;
import com.uoroot.sgi.infrastructure.api.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incidentes", description = "API para la gestión de incidentes")
public class IncidentController {

    private final IncidentService incidentService;
    private final IncidentCategoryService incidentCategoryService;
    private final IncidentCategoryResponseMapper mapper;
    private final IncidentResponseMapper incidentResponseMapper;
    private final IncidentRequestMapper incidentRequestMapper;

    @Operation(summary = "Obtener todos los incidentes", description = "Retorna una lista con todos los incidentes registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de incidentes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> getAllIncidentes() {
        try {
            List<Incident> incidents = incidentService.getAllIncidents();
            return ResponseBuilder.success(incidentResponseMapper.toIncidentResponseList(incidents));
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los incidentes: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener incidente por ID", description = "Retorna un incidente específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incidente encontrado"),
        @ApiResponse(responseCode = "404", description = "Incidente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getIncidentById(
        @Parameter(description = "ID del incidente", required = true) @PathVariable Integer id) {
        try {
            Incident incident = incidentService.getIncidentById(id);
            return ResponseBuilder.success(incidentResponseMapper.toIncidentResponse(incident));
        } catch (ResourceNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el incidente: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener incidentes por categorías", description = "Retorna todos los incidentes agrupados por categorías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías de incidentes obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/categorized")
    public ResponseEntity<?> getAllIncidentCategories() {
        try {
            var categoryIncidents = incidentCategoryService.getAllIncidentCategories();
            return ResponseBuilder.success(mapper.toListIncidentCategoryResponse(categoryIncidents));
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las categorías de incidentes: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear un nuevo incidente", description = "Crea un nuevo incidente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incidente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de incidente inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> createIncident(
        @Parameter(description = "Datos del incidente a crear", required = true)
        @RequestBody @Valid IncidentRequest incident) {
        try {
            Incident savedIncident = incidentService.saveIncident(incidentRequestMapper.toIncident(incident));
            return ResponseBuilder.success(incidentResponseMapper.toIncidentResponse(savedIncident));
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el incidente: " + e.getMessage());
        }
    }

}
