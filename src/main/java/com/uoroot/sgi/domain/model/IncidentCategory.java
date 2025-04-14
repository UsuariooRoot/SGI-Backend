package com.uoroot.sgi.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentCategory {

    private Integer id;
    private String name;
    private ITTeam itTeam;
    private List<Incident> incidents;

}
