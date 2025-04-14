package com.uoroot.sgi.domain.repository;

import java.util.List;

import com.uoroot.sgi.domain.model.Employee;

public interface EmployeeRepository {

    List<Employee> findAll(Integer itTeamId, Integer roleId);

    Employee findById(Long id);

    Employee save(Employee incident);

    void delete(Long id);
    
}
