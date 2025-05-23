package com.uoroot.sgi.infrastructure.api.mapper.ticket;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.uoroot.sgi.domain.model.History;
// import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketHistoryResponser;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketResponse;

@Mapper
public interface TicketResponseMapper {
    
    @Mapping(target = "currentHistoryId", source = "currentHistory.id")
    @Mapping(target = "assignedEmployee", source = "currentHistory.assignedEmployee")
    @Mapping(target = "status", source = "currentHistory.status")
    @Mapping(target = "priority", source = "currentHistory.priority")
    @Mapping(target = "itTeam", source = "currentHistory.team")
    TicketResponse toTicketResponse(Ticket ticket);

    // default TicketResponse.Employee toEmployee(Employee employee) {
    //     return TicketResponse.Employee.builder()
    //         .id(employee.getId())
    //         .name(employee.getName())
    //         .paternalSurname(employee.getPaternalSurname())
    //         .maternalSurname(employee.getMaternalSurname())
    //         .email(employee.getEmail())
    //         .roleId(employee.getRole().getId())
    //         .itTeamId(employee.getItTeam().getId())
    //         .build();
    // }

    List<TicketResponse> toTicketResponseList(List<Ticket> tickets);

    List<TicketHistoryResponser> toHistoryResponseList(List<History> histories);

}
