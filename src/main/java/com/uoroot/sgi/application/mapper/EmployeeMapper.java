package com.uoroot.sgi.application.mapper;

import org.springframework.stereotype.Component;

import com.uoroot.sgi.domain.model.Employee;

@Component
public class EmployeeMapper {

    public void updateEmployee(Employee source, Employee target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getPaternalSurname() != null) {
            target.setPaternalSurname(source.getPaternalSurname());
        }
        if (source.getMaternalSurname() != null) {
            target.setMaternalSurname(source.getMaternalSurname());
        }
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getRole() != null) {
            target.setRole(source.getRole());
        }
        if (source.getItTeam() != null) {
            target.setItTeam(source.getItTeam());
        }
    }

}
