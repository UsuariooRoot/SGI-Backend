package com.uoroot.sgi.infrastructure.api.mapper.incident;

import java.util.List;

import org.mapstruct.Mapper;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.infrastructure.api.dto.incident.response.IncidentResponse;

@Mapper
public interface IncidentResponseMapper {

    IncidentResponse toIncidentResponse(Incident incident);

    List<IncidentResponse> toIncidentResponseList(List<Incident> incidents);

}
