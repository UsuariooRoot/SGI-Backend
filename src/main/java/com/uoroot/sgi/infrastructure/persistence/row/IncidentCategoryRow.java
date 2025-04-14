package com.uoroot.sgi.infrastructure.persistence.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentCategoryRow {
    private Integer categoryId;
    private String categoryName;
    private Integer itTeamId;
    private String itTeamName;
    private Integer incidentId;
    private String incidentDescription;
}
