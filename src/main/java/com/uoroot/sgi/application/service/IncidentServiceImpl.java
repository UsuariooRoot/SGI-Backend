package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.repository.IncidentRepository;
import com.uoroot.sgi.domain.service.IncidentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Integer id) {
        return incidentRepository.findById(id);
    }

    public Incident saveIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

}
