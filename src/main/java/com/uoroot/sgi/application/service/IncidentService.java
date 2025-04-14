package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Integer id) {
        return incidentRepository.findById(id);
    }

    public void saveIncident(Incident incident) {
        incidentRepository.save(incident);
    }

}
