package com.uoroot.sgi.infrastructure.persistence.row.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.uoroot.sgi.domain.model.Incident;

public class IncidentRowMapper implements RowMapper<Incident> {

    @Override
    public Incident mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return Incident.builder()
                .id(rs.getInt("c_incident"))
                .description(rs.getString("x_description"))
                .categoryId(rs.getInt("c_category"))
                .priorityId(rs.getInt("c_priority"))
                .build();
    }
}
