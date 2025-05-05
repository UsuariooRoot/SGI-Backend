package com.uoroot.sgi.domain.service;

import java.util.List;

import com.uoroot.sgi.domain.model.Incident;

public interface IncidentService {

    List<Incident> getAllIncidents();

    Incident getIncidentById(Integer id);

    Incident saveIncident(Incident incident);

}
