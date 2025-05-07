package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Priority;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.domain.repository.TicketRepository;
import com.uoroot.sgi.infrastructure.persistence.row.mapper.HistoryRowMapper;
import com.uoroot.sgi.infrastructure.persistence.row.mapper.TicketRowMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcTicketRepository implements TicketRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @SuppressWarnings("null")
    @Override
    public List<Ticket> findAll(Ticket.Filter filter) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "SELECT * FROM ufn_filter_tickets(:showNewTickets, :statusIds, :assignedEmployeeId, :ownerEmployeeId, :dateFrom, :dateTo, :itTeamId)";

        params.addValue("showNewTickets", filter.isShowNewTickets());

        // Handle status IDs array
        Array statusIdsArray = buildSqlArray(filter.getStatusIds(), "integer");

        params.addValue("statusIds", statusIdsArray);
        params.addValue("assignedEmployeeId", filter.getAssignedEmployeeId());
        params.addValue("ownerEmployeeId", filter.getOwnerEmployeeId());

        // Handle date parameters
        params.addValue("dateFrom", filter.getDateFrom() != null ? Timestamp.valueOf(filter.getDateFrom()) : null);
        params.addValue("dateTo", filter.getDateTo() != null ? Timestamp.valueOf(filter.getDateTo()) : null);

        params.addValue("itTeamId", filter.getItTeamId());

        // Query execution and mapping
        List<Ticket> tickets = this.namedJdbcTemplate.query(sql, params, new TicketRowMapper());
        System.out.println("tickets: " + tickets);

        if (tickets.isEmpty()) {
            return tickets;
        }
        
        List<Long> historiesIds = tickets.stream()
                .map(ticket -> ticket.getCurrentHistory().getId())
                .toList();

        Array currentHistoriesIdsArray = buildSqlArray(historiesIds, "bigint");
        String historySql = "SELECT * FROM ufn_find_ticket_info_by_history_ids(:historyIds)";

        MapSqlParameterSource historyParams = new MapSqlParameterSource();
        historyParams.addValue("historyIds", currentHistoriesIdsArray);

        Map<Long, HistoryInfo> historyInfoMap = namedJdbcTemplate.query(
        historySql, 
        historyParams,
        (rs, rowNum) -> {
            Long historyId = rs.getLong("history_id");
            
            // Empleado asignado
            Employee assignedEmployee = null;
            if (rs.getObject("assigned_employee_id") != null) {
                assignedEmployee = Employee.builder()
                    .id(rs.getLong("assigned_employee_id"))
                    .name(rs.getString("assigned_employee_name"))
                    .paternalSurname(rs.getString("assigned_employee_paternal_surname"))
                    .maternalSurname(rs.getString("assigned_employee_maternal_surname"))
                    .email(rs.getString("assigned_employee_email"))
                    .build();
            }
            
            // Status
            Status status = new Status(
                rs.getInt("status_id"),
                rs.getString("status_name")
            );
            
            // Priority
            Priority priority = new Priority(
                rs.getInt("priority_id"),
                rs.getString("priority_name")
            );
            
            // IT Team
            ITTeam team = new ITTeam(
                rs.getInt("team_id"),
                rs.getString("team_name")
            );
            
            return new AbstractMap.SimpleEntry<>(
                historyId, 
                new HistoryInfo(assignedEmployee, status, priority, team)
            );
        }).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        // Actualizar los tickets con la información de los historiales
        for (Ticket ticket : tickets) {
            HistoryInfo historyInfo = historyInfoMap.get(ticket.getCurrentHistory().getId());
            if (historyInfo != null) {
                ticket.getCurrentHistory().setAssignedEmployee(historyInfo.assignedEmployee());
                ticket.getCurrentHistory().setStatus(historyInfo.status());
                ticket.getCurrentHistory().setPriority(historyInfo.priority());
                ticket.getCurrentHistory().setTeam(historyInfo.team());
            }
        }

        return tickets;
    }

    @Override
    public Ticket findById(Long id) {
        String sql = "SELECT * FROM ufn_find_ticket_by_id(:id)";
        List<Ticket> tickets = this.namedJdbcTemplate.query(sql, new MapSqlParameterSource("id", id),
                new TicketRowMapper());
        return tickets.isEmpty() ? null : tickets.get(0);
    }

    @Override
    public History findHistoryById(Long historyId) {
        String sql = "SELECT * FROM ufn_find_history_by_id(:historyId)";

        List<History> histories = this.namedJdbcTemplate.query(sql, new MapSqlParameterSource("historyId", historyId),
                new HistoryRowMapper());
        return histories.isEmpty() ? null : histories.get(0);
    }

    @Override
    public List<History> findAllHistoryByTicketId(Long id) {
        String sql = "SELECT * FROM ufn_find_all_history_by_ticket_id(:id)";
        List<History> histories = this.namedJdbcTemplate.query(sql, new MapSqlParameterSource("id", id),
                new HistoryRowMapper());
        return histories;
    }

    private <T> Array buildSqlArray(List<T> items, String sqlTypeName) {
        if (items == null || items.isEmpty()) {
            return null;
        }

        try (Connection conn = DataSourceUtils.getConnection(
                this.namedJdbcTemplate.getJdbcTemplate().getDataSource())) {
            return conn.createArrayOf(sqlTypeName, items.toArray());
        } catch (SQLException e) {
            return null;
        }
    }

    private record HistoryInfo(
        Employee assignedEmployee,
        Status status,
        Priority priority,
        ITTeam team
    ) {}


}