package com.uoroot.sgi.infrastructure.persistence.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.uoroot.sgi.domain.model.User;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.model.ITTeam;
import com.uoroot.sgi.domain.model.Role;
import com.uoroot.sgi.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM ufn_get_user_by_name(?)";
        return namedJdbcTemplate
                .getJdbcTemplate()
                .queryForObject(sql, userRowMapper(), new Object[] { username });
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE x_username = ?";
        Integer count = namedJdbcTemplate
                .getJdbcTemplate()
                .queryForObject(sql, Integer.class, new Object[] { username });
        return count != null && count > 0;
    }

    @Override
    public User save(User user) {
        insertUser(user);
        return findByUsername(user.getUsername());
    }

    private void insertUser(User user) {
        String sql = "INSERT INTO users (x_username, x_password, c_employee) VALUES (:username, :password, :employeeId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("employeeId", user.getEmployee().getId());

        namedJdbcTemplate.update(sql, params);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            Employee employee = Employee.builder()
                    .id(rs.getLong("employee_id"))
                    .name(rs.getString("name"))
                    .paternalSurname(rs.getString("paternal_surname"))
                    .maternalSurname(rs.getString("maternal_surname"))
                    .email(rs.getString("email"))
                    .role(Role.builder()
                            .id(rs.getInt("role_id"))
                            .name(rs.getString("role_name"))
                            .build())
                    .itTeam(ITTeam.builder()
                            .id(rs.getInt("it_team_id"))
                            .name(rs.getString("it_team_name"))
                            .build())
                    .build();

            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmployee(employee);

            return user;
        };
    }

}
