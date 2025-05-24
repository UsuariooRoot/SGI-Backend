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

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = {"https://tu-dominio-angular.com", "http://localhost:4200"})
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final IncidentCategoryService incidentCategoryService;
    private final IncidentCategoryResponseMapper mapper;
    private final IncidentResponseMapper incidentResponseMapper;
    private final IncidentRequestMapper incidentRequestMapper;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getIncidentById(@PathVariable Integer id) {
        try {
            Incident incident = incidentService.getIncidentById(id);
            return ResponseBuilder.success(incidentResponseMapper.toIncidentResponse(incident));
        } catch (ResourceNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el incidente: " + e.getMessage());
        }
    }

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

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> createIncident(@RequestBody @Valid IncidentRequest incident) {
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
