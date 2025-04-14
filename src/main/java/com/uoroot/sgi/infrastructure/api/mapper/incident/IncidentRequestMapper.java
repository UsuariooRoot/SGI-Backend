package com.uoroot.sgi.infrastructure.api.mapper.incident;

import org.mapstruct.Mapper;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.infrastructure.api.dto.incident.request.IncidentRequest;

@Mapper
public interface IncidentRequestMapper {

    Incident toIncident(IncidentRequest incidentRequest);

}
