package com.uoroot.sgi.infrastructure.api.dto.incident.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentRequest {
    private String description;
    private Integer categoryId;
    private Integer priorityId;
}
