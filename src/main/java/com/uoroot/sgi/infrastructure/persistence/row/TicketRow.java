package com.uoroot.sgi.infrastructure.persistence.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.model.Role;
import com.uoroot.sgi.domain.model.Ticket;

public class TicketRow implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        // Map Employee creator
        Employee creator = Employee.builder()
                .id(rs.getLong("c_employee_creator"))
                .name(rs.getString("creator_name"))
                .paternalSurname(rs.getString("creator_paternal_surname"))
                .maternalSurname(rs.getString("creator_maternal_surname"))
                .email(rs.getString("creator_email"))
                .role(Role.builder()
                        .id(rs.getInt("creator_role_id"))
                        .build())
                .itTeam(ITTeam.builder()
                        .id(rs.getInt("creator_it_team_id"))
                        .build())
                .build();

        // Map Employee owner
        Employee owner = Employee.builder()
                .id(rs.getLong("c_employee_owner"))
                .name(rs.getString("owner_name"))
                .paternalSurname(rs.getString("owner_paternal_surname"))
                .maternalSurname(rs.getString("owner_maternal_surname"))
                .email(rs.getString("owner_email"))
                .role(Role.builder()
                        .id(rs.getInt("owner_role_id"))
                        .build())
                .itTeam(ITTeam.builder()
                        .id(rs.getInt("owner_it_team_id"))
                        .build())
                .build();

        // Map Incident
        Incident incident = Incident.builder()
                .id(rs.getInt("c_incident"))
                .description(rs.getString("incident_description"))
                .categoryId(rs.getInt("incident_category_id"))
                .priorityId(rs.getInt("incident_priority_id"))
                .build();

        // Create the ticket without history (will be loaded separately if needed)
        return Ticket.builder()
                .id(rs.getLong("c_ticket"))
                .creator(creator)
                .owner(owner)
                .incident(incident)
                .description(rs.getString("x_ticket_description"))
                .created(rs.getTimestamp("f_created").toLocalDateTime())
                .build();
    }
}
