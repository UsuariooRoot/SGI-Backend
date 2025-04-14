package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.domain.model.IncidentCategory;
import com.uoroot.sgi.domain.repository.IncidentCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentCategoryService {
    private final IncidentCategoryRepository incidentCategoryRepository;

    public List<IncidentCategory> getAllIncidentCategories() {
        return incidentCategoryRepository.findAll();
    }
}
