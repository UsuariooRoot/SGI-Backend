package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.EmployeeNotFoundException;
import com.uoroot.sgi.domain.service.EmployeeService;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.infrastructure.api.dto.employee.request.EmployeRequest;
import com.uoroot.sgi.infrastructure.api.dto.employee.response.EmployeeResponse;
import com.uoroot.sgi.infrastructure.api.mapper.employee.EmployeRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.employee.EmployeeResponseMapper;
import com.uoroot.sgi.infrastructure.api.util.ResponseBuilder;

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
    public ResponseEntity<?> getEmployees(
            @RequestParam(required = false, name = "it_team_id") Integer itTeamId,
            @RequestParam(required = false, name = "role_id") Integer roleId) {
        try {
            List<EmployeeResponse> employees = employeeResponseMapper
                    .toEmployeeResponseList(employeeService.getEmployees(itTeamId, roleId));
            return ResponseBuilder.success(employees);
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener empleados: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseBuilder.success(employeeResponseMapper.toEmployeeResponse(employee));
        } catch (EmployeeNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el empleado: " + e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid EmployeRequest employee) {
        try {
            Employee mappedEmployee = employeRequestMapper.toEmployee(employee);
            Employee createdEmployee = employeeService.createEmployee(mappedEmployee);
            return ResponseBuilder.success(employeeResponseMapper.toEmployeeResponse(createdEmployee));
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el empleado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseBuilder.success(updatedEmployee);
        } catch (EmployeeNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el empleado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseBuilder.success("Empleado con id " + id + " eliminado exitosamente");
        } catch (EmployeeNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el empleado: " + e.getMessage());
        }
    }

}
