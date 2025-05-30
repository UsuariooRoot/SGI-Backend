package com.uoroot.sgi.infrastructure.api.mapper.incident;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.model.IncidentCategory;
import com.uoroot.sgi.infrastructure.api.dto.incident.response.IncidentCategoryResponse;
import com.uoroot.sgi.infrastructure.api.dto.incident.response.IncidentCategoryResponse.ITTeam;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IncidentCategoryResponseMapper {

    public List<IncidentCategoryResponse> toListIncidentCategoryResponse(List<IncidentCategory> categories) {
        return categories.stream()
                .map(this::toIncidentCategoryResponse)
                .collect(Collectors.toList());
    }

    private IncidentCategoryResponse toIncidentCategoryResponse(IncidentCategory category) {

        ITTeam itTeam = ITTeam.builder()
                .id(category.getItTeam().getId())
                .name(category.getItTeam().getName())
                .build();

        return IncidentCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .itTeam(itTeam)
                .incidents(category.getIncidents().stream()
                        .map(this::toIncidentResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private IncidentCategoryResponse.Incident toIncidentResponse(Incident incident) {
        return IncidentCategoryResponse.Incident.builder()
                .id(incident.getId())
                .description(incident.getDescription())
                .build();
    }

}
