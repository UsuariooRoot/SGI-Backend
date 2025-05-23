package com.uoroot.sgi.domain.service;

import java.util.List;
import com.uoroot.sgi.domain.model.Employee;

public interface EmployeeService {

    List<Employee> getEmployees(Integer itTeamId, Integer roleId);

    Employee getEmployeeById(Long employeeId);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long employeeId, Employee employee);

    public void deleteEmployee(Long employeeId);

}
