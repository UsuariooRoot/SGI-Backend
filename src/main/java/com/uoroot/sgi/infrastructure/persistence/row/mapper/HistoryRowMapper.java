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
import com.uoroot.sgi.domain.model.Ticket;

public class HistoryRowMapper implements RowMapper<History> {

    @Override
    @Nullable
    public History mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
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

            return History.builder()
                    .id(rs.getLong("c_history"))
                    .ticket(Ticket.builder().id(rs.getLong("c_ticket")).build())
                    .employee(Employee.builder().id(rs.getLong("c_employee")).build())
                    .assignedEmployee(Employee.builder().id(rs.getLong("c_employee_assigned")).build())
                    .action(action)
                    .status(status)
                    .priority(priority)
                    .team(team)
                    .comment(rs.getString("x_comment"))
                    .logged(rs.getTimestamp("f_logged").toLocalDateTime())
                    .build();
    }
    

}
