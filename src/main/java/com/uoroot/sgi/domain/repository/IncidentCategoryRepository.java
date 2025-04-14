package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.IncidentCategory;

public interface IncidentCategoryRepository {

    List<IncidentCategory> findAll();

}
