package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.application.service.IncidentCategoryService;
import com.uoroot.sgi.application.service.IncidentService;
import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.infrastructure.api.dto.ApiResponse;
import com.uoroot.sgi.infrastructure.api.dto.incident.request.IncidentRequest;
import com.uoroot.sgi.infrastructure.api.dto.incident.response.IncidentCategoryResponse;
import com.uoroot.sgi.infrastructure.api.dto.incident.response.IncidentResponse;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentCategoryResponseMapper;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.incident.IncidentResponseMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final IncidentCategoryService incidentCategoryService;
    private final IncidentCategoryResponseMapper mapper;
    private final IncidentResponseMapper incidentResponseMapper;
    private final IncidentRequestMapper incidentRequestMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> getAllIncidentes() {
        List<Incident> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(new ApiResponse<>(
                incidentResponseMapper.toIncidentResponseList(incidents),
                incidents.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(@PathVariable Integer id) {
        Incident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(incidentResponseMapper.toIncidentResponse(incident));
    }

    @GetMapping("/categorized")
    public ResponseEntity<List<IncidentCategoryResponse>> getAllIncidentCategories() {
        var categoryIncidents = incidentCategoryService.getAllIncidentCategories();
        return ResponseEntity.ok(mapper.toListIncidentCategoryResponse(categoryIncidents));
    }

    @PostMapping()
    public ResponseEntity<IncidentRequest> createIncident(@RequestBody IncidentRequest incident) {
        incidentService.saveIncident(incidentRequestMapper.toIncident(incident));
        return ResponseEntity.ok(incident);
    }

}
