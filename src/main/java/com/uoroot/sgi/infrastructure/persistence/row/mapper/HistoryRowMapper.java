package com.uoroot.sgi.infrastructure.persistence.row.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.uoroot.sgi.domain.model.Action;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Priority;
import com.uoroot.sgi.domain.model.Status;

public class HistoryRowMapper implements RowMapper<History> {

    @Override
    @Nullable
    public History mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Employee employee = Employee.builder()
        .id(rs.getLong("employee_id"))
        .name(rs.getString("employee_name"))
        .paternalSurname(rs.getString("employee_paternal_surname"))
        .maternalSurname(rs.getString("employee_maternal_surname"))
        .email(rs.getString("employee_email"))
        .build();
        
        Employee assignedEmployee = null;
        if (rs.getString("assigned_employee_id") != null) {
            assignedEmployee = Employee.builder()
            .id(rs.getLong("assigned_employee_id"))
            .name(rs.getString("assigned_employee_name"))
            .paternalSurname(rs.getString("assigned_employee_paternal_surname"))
            .maternalSurname(rs.getString("assigned_employee_maternal_surname"))
            .email(rs.getString("assigned_employee_email"))
            .build();
        }

        Action action = new Action(
                rs.getInt("action_id"),
                rs.getString("action_name"));

        Status status = new Status(
                rs.getInt("status_id"),
                rs.getString("status_name"));

        Priority priority = new Priority(
                rs.getInt("priority_id"),
                rs.getString("priority_name"));

        ITTeam team = new ITTeam(
                rs.getInt("team_id"),
                rs.getString("team_name"));

        History history = History.builder()
                .id(rs.getLong("history_id"))
                .employee(employee)
                .assignedEmployee(assignedEmployee)
                .action(action)
                .status(status)
                .priority(priority)
                .team(team)
                .comment(rs.getString("comment"))
                .logged(rs.getTimestamp("logged").toLocalDateTime())
                .build();

        return history;
    }

}
