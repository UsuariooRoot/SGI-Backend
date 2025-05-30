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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Empleados", description = "API para la gestión de empleados")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeRequestMapper employeRequestMapper;
    private final EmployeeResponseMapper employeeResponseMapper;

    @Operation(summary = "Obtener todos los empleados", description = "Retorna una lista de empleados con filtros opcionales por equipo de TI y rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente",
                     content = @Content(mediaType = "application/json",
                     array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> getEmployees(
            @Parameter(description = "ID del equipo de TI (opcional)") 
            @RequestParam(required = false, name = "it_team_id") Integer itTeamId,
            @Parameter(description = "ID del rol (opcional)") 
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

    @Operation(summary = "Obtener empleado por ID", description = "Retorna un empleado específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = EmployeeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(
        @Parameter(description = "ID del empleado", required = true) @PathVariable Long id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseBuilder.success(employeeResponseMapper.toEmployeeResponse(employee));
        } catch (EmployeeNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el empleado: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear un nuevo empleado", description = "Crea un nuevo empleado en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado creado exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = EmployeeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de empleado inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> createEmployee(
        @Parameter(description = "Datos del empleado a crear", required = true)
        @RequestBody @Valid EmployeRequest employee) {
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

    @Operation(summary = "Actualizar empleado", description = "Actualiza los datos de un empleado existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Employee.class))),
        @ApiResponse(responseCode = "400", description = "Datos de empleado inválidos"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> updateEmployee(
        @Parameter(description = "ID del empleado a actualizar", required = true) @PathVariable Long id, 
        @Parameter(description = "Datos actualizados del empleado", required = true) @RequestBody @Valid Employee employee) {
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

    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar el empleado"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<?> deleteEmployee(
        @Parameter(description = "ID del empleado a eliminar", required = true) @PathVariable Long id) {
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
