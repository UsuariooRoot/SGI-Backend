package com.uoroot.sgi.infrastructure.api.mapper.employee;

import java.util.List;

import org.mapstruct.Mapper;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.infrastructure.api.dto.employee.response.EmployeeResponse;

@Mapper
public interface EmployeeResponseMapper {
    
    EmployeeResponse toEmployeeResponse(Employee employee);

    List<EmployeeResponse> toEmployeeResponseList(List<Employee> employees);
}
