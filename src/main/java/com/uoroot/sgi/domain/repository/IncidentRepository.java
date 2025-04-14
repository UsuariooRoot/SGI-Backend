package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.Incident;

public interface IncidentRepository {

    List<Incident> findAll();

    Incident findById(Integer id);

    Incident save(Incident incident);

    void delete(Integer id);

}
