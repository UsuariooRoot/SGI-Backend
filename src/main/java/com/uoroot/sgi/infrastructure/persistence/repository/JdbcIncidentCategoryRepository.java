package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.IncidentCategory;
import com.uoroot.sgi.domain.repository.IncidentCategoryRepository;
import com.uoroot.sgi.infrastructure.persistence.mapper.IncidentCategoryMapper;
import com.uoroot.sgi.infrastructure.persistence.row.IncidentCategoryRow;
import com.uoroot.sgi.infrastructure.persistence.row.mapper.IncidentCategoryRowMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcIncidentCategoryRepository implements IncidentCategoryRepository {

    private final IncidentCategoryMapper incidentCategoryResponseMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<IncidentCategory> findAll() {
        String sql = "SELECT * FROM ufn_get_category_team_and_incidents()";
        List<IncidentCategoryRow> rows = jdbcTemplate.query(sql, new IncidentCategoryRowMapper());
        
        return incidentCategoryResponseMapper.toListIncidentCategory(rows);
    }

    @Override
    public IncidentCategory findById(Integer id) {
        List<IncidentCategory> categories = findAll();
        return categories.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
