package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.History;
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
        String sql = "SELECT * FROM ufn_filter_tickets(:showNewTickets, :statusIds, :assignedEmployeeId, :ownerEmployeeId, :dateFrom, :dateTo)";

        params.addValue("showNewTickets", filter.isShowNewTickets());

        // Handle status IDs array
        Array statusIdsArray = null;
        if (filter.getStatusIds() != null && !filter.getStatusIds().isEmpty()) {
            try {
                statusIdsArray = this.namedJdbcTemplate.getJdbcTemplate().getDataSource()
                        .getConnection()
                        .createArrayOf("integer",
                                filter.getStatusIds().toArray(new Integer[0]));
            } catch (SQLException e) {
                throw new RuntimeException("Error creating SQL array for status IDs", e);
            }
        }
        params.addValue("statusIds", statusIdsArray);
        params.addValue("assignedEmployeeId", filter.getAssignedEmployeeId());
        params.addValue("ownerEmployeeId", filter.getOwnerEmployeeId());

        // Handle date parameters
        params.addValue("dateFrom", filter.getDateFrom() != null ? Timestamp.valueOf(filter.getDateFrom()) : null);
        params.addValue("dateTo", filter.getDateTo() != null ? Timestamp.valueOf(filter.getDateTo()) : null);

        // Query execution and mapping
        List<Ticket> tickets = this.namedJdbcTemplate.query(sql, params, new TicketRowMapper());

        // for (Ticket ticket : tickets) {
        // History history = findHistoryById(ticket.getCurrentHistory().getId());
        // ticket.setCurrentHistory(history);
        // }

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

}