package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.Incident;
import com.uoroot.sgi.domain.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcIncidentRepository implements IncidentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Incident> findAll() {
        String sql = "SELECT c_incident, x_description, c_category, c_priority FROM Incidents";
        return jdbcTemplate.query(sql, incidentRowMapper());
    }

    @Override
    public Incident findById(Integer id) {
        String sql = "SELECT c_incident, x_description, c_category, c_priority FROM Incidents WHERE c_incident = ?";
        return jdbcTemplate.queryForObject(sql, incidentRowMapper(), id);
    }

    @Override
    public Incident save(Incident incident) {
        if (incident.getId() == null) {
            // Insert
            String sql = "INSERT INTO Incidents (x_description, c_category, c_priority) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, incident.getDescription(), incident.getCategoryId(), incident.getPriorityId());

            Integer id = jdbcTemplate.queryForObject("SELECT lastval()", Integer.class);
            incident.setId(id);
        } else {
            // Update
            String sql = "UPDATE Incidents SET x_description = ?, c_category = ?, c_priority = ? WHERE c_incident = ?";
            jdbcTemplate.update(sql, incident.getDescription(), incident.getCategoryId(), incident.getPriorityId(), incident.getId());
        }
        return incident;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Incidents WHERE c_incident = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Incident> incidentRowMapper() {
        return (rs, rowNum) -> Incident.builder()
                .id(rs.getInt("c_incident"))
                .description(rs.getString("x_description"))
                .categoryId(rs.getInt("c_category"))
                .priorityId(rs.getInt("c_priority"))
                .build();
    }
}
