package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.Action;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.FilterTicket;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.model.Priority;
import com.uoroot.sgi.domain.model.Role;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.domain.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcTicketRepository implements TicketRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Ticket> findAll(FilterTicket filter) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM ufn_filter_tickets(?, ?, ?, ?, ?, ?)");

        params.add(filter.isShowNewTickets());

        // Handle status IDs array
        Array statusIdsArray = null;
        if (filter.getStatusIds() != null && !filter.getStatusIds().isEmpty()) {
            try {
                statusIdsArray = this.jdbcTemplate.getDataSource()
                        .getConnection()
                        .createArrayOf("integer",
                                filter.getStatusIds().toArray(new Integer[0]));
            } catch (SQLException e) {
                throw new RuntimeException("Error creating SQL array for status IDs", e);
            }
        }
        params.add(statusIdsArray);

        params.add(filter.getAssignedEmployeeId());
        params.add(filter.getOwnerEmployeeId());

        // Handle date parameters
        params.add(filter.getDateFrom() != null ? Timestamp.valueOf(filter.getDateFrom()) : null);
        params.add(filter.getDateTo() != null ? Timestamp.valueOf(filter.getDateTo()) : null);

        // Query execution and mapping

        List<Ticket> tickets = jdbcTemplate.query(sql.toString(), ticketRowMapper(), params.toArray());

        // for (Ticket ticket : tickets) {
        //     History history = findHistoryById(ticket.getCurrentHistory().getId());
        //     ticket.setCurrentHistory(history);
        // }

        return tickets;
    }

    @Override
    public Ticket findById(Long id) {
        String sql = "SELECT * FROM ufn_find_ticket_by_id(?)";
        List<Ticket> tickets = jdbcTemplate.query(sql, ticketRowMapper(), new Object[]{id});
        return tickets.isEmpty() ? null : tickets.get(0);
    }

    @Override
    public History findHistoryById(Long historyId) {
        String sql = "SELECT * FROM ufn_find_history_by_id(?)";
        
        List<History> histories = jdbcTemplate.query(sql,historyRowMapper(), new Object[]{historyId});
        return histories.isEmpty() ? null : histories.get(0);
    }

    private RowMapper<Ticket> ticketRowMapper() {
        return (rs, rowNum) -> {
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
                    .currentHistory(History.builder().id(rs.getLong("n_current_history")).build())
                    .build();
        };
    }

    private RowMapper<History> historyRowMapper() {
        return (rs, rowNum) -> {
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
        };
    }

}