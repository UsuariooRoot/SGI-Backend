package com.uoroot.sgi.infrastructure.persistence.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Role;

public class EmployeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Role role = Role.builder()
                .id(rs.getInt("c_role"))
                .name(rs.getString("role_name"))
                .build();

        ITTeam itTeam = rs.getObject("c_it_team") != null
                ? ITTeam.builder()
                        .id(rs.getInt("c_it_team"))
                        .name(rs.getString("team_name"))
                        .build()
                : null;

        return Employee.builder()
                .id(rs.getLong("c_employee"))
                .name(rs.getString("x_name"))
                .paternalSurname(rs.getString("x_paternal_surname"))
                .maternalSurname(rs.getString("x_maternal_surname"))
                .email(rs.getString("x_email"))
                .role(role)
                .itTeam(itTeam)
                .build();
    }
}
