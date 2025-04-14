package com.uoroot.sgi.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.model.IncidentCategory;
import com.uoroot.sgi.infrastructure.persistence.row.IncidentCategoryRow;

@Component
public class IncidentCategoryMapper {

    public List<IncidentCategory> toListIncidentCategory(List<IncidentCategoryRow> rows) {
        Map<Integer, IncidentCategory> categoryMap = new LinkedHashMap<>();

        for (IncidentCategoryRow row : rows) {
            IncidentCategory category = categoryMap.computeIfAbsent(row.getCategoryId(),
                    id -> IncidentCategory.builder()
                            .id(row.getCategoryId())
                            .name(row.getCategoryName())
                            .itTeam(ITTeam.builder()
                                    .id((row.getItTeamId()))
                                    .name(row.getItTeamName())
                                    .build())
                            .incidents(new ArrayList<>())
                            .build());

            if (row.getIncidentId() != null) {
                category.getIncidents().add(
                        Incident.builder()
                                .id(row.getIncidentId())
                                .description(row.getIncidentDescription())
                                .build());
            }
        }

        return new ArrayList<>(categoryMap.values());
    }

}
