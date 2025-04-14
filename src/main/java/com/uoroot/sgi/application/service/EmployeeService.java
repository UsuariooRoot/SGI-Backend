package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.application.mapper.EmployeeMapper;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public List<Employee> getEmployees(Integer itTeamId, Integer roleId) {
        return employeeRepository.findAll(itTeamId, roleId);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id);
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        employeeMapper.updateEmployee(employee, existingEmployee);
        existingEmployee.setId(id);

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.delete(id);
    }

}
