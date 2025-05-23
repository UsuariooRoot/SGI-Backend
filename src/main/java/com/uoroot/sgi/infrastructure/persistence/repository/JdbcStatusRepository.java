package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.repository.StatusRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcStatusRepository implements StatusRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Status> findAll() {
        String sql = "SELECT * FROM TicketStatuses";
        return jdbcTemplate.query(sql, statusRowMapper());
    }

    @Override
    public Status findById(Integer id) {
        String sql = "SELECT * FROM TicketStatuses WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, statusRowMapper(), new Object[] { id });
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM TicketStatuses WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { id } );
        return count != null && count > 0;
    }
    
    private RowMapper<Status> statusRowMapper() {
        return (rs, rowNum) -> {
            Status status = new Status();
            status.setId(rs.getInt("c_status"));
            status.setName(rs.getString("x_name"));
            return status;
        };
    }

}
