package com.uoroot.sgi.infrastructure.api.dto.incident.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentResponse {

    private Integer id;
    private String description;
    private Integer categoryId;
    private Integer priorityId;

}
