package com.uoroot.sgi.infrastructure.persistence.row.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.uoroot.sgi.infrastructure.persistence.row.IncidentCategoryRow;

public class IncidentCategoryRowMapper implements RowMapper<IncidentCategoryRow> {

    @Override
    public IncidentCategoryRow mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return IncidentCategoryRow.builder()
                .categoryId(rs.getInt("category_id"))
                .categoryName(rs.getString("category_name"))
                .itTeamId(rs.getInt("it_team_id"))
                .itTeamName(rs.getString("it_team_name"))
                .incidentId(rs.getInt("incident_id"))
                .incidentDescription(rs.getString("incident_description"))
                .build();
    }

}
