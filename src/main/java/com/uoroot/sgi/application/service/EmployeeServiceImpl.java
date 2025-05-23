package com.uoroot.sgi.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uoroot.sgi.application.mapper.EmployeeMapper;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.repository.EmployeeRepository;
import com.uoroot.sgi.domain.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public List<Employee> getEmployees(Integer itTeamId, Integer roleId) {
        return employeeRepository.findAll(itTeamId, roleId);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long employeeId, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        employeeMapper.updateEmployee(employee, existingEmployee);
        existingEmployee.setId(employeeId);

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.delete(employeeId);
    }

}
