package com.uoroot.sgi.infrastructure.persistence.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

public class IncidentCategoryRowMapper implements RowMapper<IncidentCategoryRow> {

    @Override
    public IncidentCategoryRow mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new IncidentCategoryRow(
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getInt("it_team_id"),
                rs.getString("it_team_name"),
                rs.getObject("incident_id") != null ? rs.getInt("incident_id") : null,
                rs.getString("incident_description"));
    }

}
