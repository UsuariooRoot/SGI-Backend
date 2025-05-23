package com.uoroot.sgi.infrastructure.api.dto.incident.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentCategoryResponse {

    private Integer id;
    private String name;
    private ITTeam itTeam;
    private List<Incident> incidents;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ITTeam {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Incident {
        private Integer id;
        private String description;
    }

}