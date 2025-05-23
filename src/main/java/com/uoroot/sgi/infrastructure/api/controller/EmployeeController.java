package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.service.EmployeeService;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.infrastructure.api.dto.ApiResponse;
import com.uoroot.sgi.infrastructure.api.dto.employee.request.EmployeRequest;
import com.uoroot.sgi.infrastructure.api.dto.employee.response.EmployeeResponse;
import com.uoroot.sgi.infrastructure.api.mapper.employee.EmployeRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.employee.EmployeeResponseMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeRequestMapper employeRequestMapper;
    private final EmployeeResponseMapper employeeResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployees(
            @RequestParam(required = false, name = "it_team_id") Integer itTeamId,
            @RequestParam(required = false, name = "role_id") Integer roleId) {
        List<EmployeeResponse> employees = employeeResponseMapper
                .toEmployeeResponseList(employeeService.getEmployees(itTeamId, roleId));
        return ResponseEntity.ok(new ApiResponse<>(employees, employees.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeResponseMapper.toEmployeeResponse(employee));
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeRequest employee) {
        Employee mappedEmployee = employeRequestMapper.toEmployee(employee);
        Employee createdEmployee = employeeService.createEmployee(mappedEmployee);
        return ResponseEntity.ok(employeeResponseMapper.toEmployeeResponse(createdEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee with id " + id + " deleted successfully");
    }

}
