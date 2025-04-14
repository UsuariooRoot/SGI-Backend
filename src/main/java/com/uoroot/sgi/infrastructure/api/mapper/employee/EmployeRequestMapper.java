package com.uoroot.sgi.infrastructure.api.mapper.employee;

import org.springframework.stereotype.Component;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Role;
import com.uoroot.sgi.infrastructure.api.dto.employee.request.EmployeRequest;

@Component
public class EmployeRequestMapper {

    public Employee toEmployee(EmployeRequest request) {
        Role role = new Role(request.getRoleId(), null);
        ITTeam itTeam = new ITTeam(request.getItTeamId(), null);

        return Employee.builder()
                .name(request.getName())
                .paternalSurname(request.getPaternalSurname())
                .maternalSurname(request.getMaternalSurname())
                .email(request.getEmail())
                .role(role)
                .itTeam(itTeam)
                .build();
    }
}
