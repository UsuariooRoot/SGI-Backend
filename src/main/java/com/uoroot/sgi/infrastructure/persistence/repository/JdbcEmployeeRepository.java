package com.uoroot.sgi.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.repository.EmployeeRepository;
import com.uoroot.sgi.infrastructure.persistence.row.EmployeRowMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JdbcEmployeeRepository implements EmployeeRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Employee> findAll(Integer itTeamId, Integer roleId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ufn_filter_employees(:itTeamId, :roleId)");

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("itTeamId", itTeamId);
        params.addValue("roleId", roleId);

        return namedJdbcTemplate.query(sql.toString(), params, new EmployeRowMapper());
    }

    @Override
    public Employee findById(Long id) {
        String sql = "ufn_get_employee_by_id(:employeeId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("employeeId", id);

        return namedJdbcTemplate.queryForObject(sql, params, new EmployeRowMapper());
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            insertEmployee(employee);
            Long id = namedJdbcTemplate.getJdbcTemplate().queryForObject("SELECT lastval()", Long.class);
            employee.setId(id);
        } else {
            updateEmployee(employee);
        }
        return employee;
    }

    private void insertEmployee(Employee employee) {
        String sql = """
                INSERT INTO Employees (x_name, x_paternal_surname, x_maternal_surname, x_email, n_role, c_it_team)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        namedJdbcTemplate.getJdbcTemplate().update(sql,
                employee.getName(),
                employee.getPaternalSurname(),
                employee.getMaternalSurname(),
                employee.getEmail(),
                employee.getRole().getId(),
                employee.getItTeam() != null ? employee.getItTeam().getId() : null);
    }

    private void updateEmployee(Employee employee) {
        String sql = """
                UPDATE Employees
                SET x_name = ?, x_paternal_surname = ?, x_maternal_surname = ?, x_email = ?, n_role = ?, c_it_team = ?
                WHERE c_employee = ?
                """;

        namedJdbcTemplate.getJdbcTemplate().update(sql,
                employee.getName(),
                employee.getPaternalSurname(),
                employee.getMaternalSurname(),
                employee.getEmail(),
                employee.getRole().getId(),
                employee.getItTeam() != null ? employee.getItTeam().getId() : null,
                employee.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM Employees WHERE c_employee = ?";
        namedJdbcTemplate.getJdbcTemplate().update(sql, id);
    }
}
