package com.uoroot.sgi.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.service.TicketService;
import com.uoroot.sgi.domain.model.History;
import com.uoroot.sgi.domain.model.Status;
import com.uoroot.sgi.domain.model.Ticket;
import com.uoroot.sgi.infrastructure.api.dto.CustomApiResponse;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.ActionFormRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.CreateTicketFormRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.request.FilterTicketRequest;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketHistoryResponser;
import com.uoroot.sgi.infrastructure.api.dto.ticket.response.TicketResponse;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketRequestMapper;
import com.uoroot.sgi.infrastructure.api.mapper.ticket.TicketResponseMapper;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "API para la gestión de tickets de incidentes y su ciclo de vida")
public class TicketController {

    private final TicketService ticketService;
    private final TicketRequestMapper ticketRequestMapper;
    private final TicketResponseMapper ticketResponseMapper;

    @Operation(
        summary = "Obtener todos los tickets", 
        description = "Retorna una lista de tickets filtrados según los criterios proporcionados como parámetros de consulta. Permite filtrar por estado, prioridad, fecha de creación, etc.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de tickets obtenida exitosamente",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketResponse.class)))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Prohibido - No tiene permisos para acceder a este recurso",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"Acceso denegado\"}")
            )
        )
    })
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<TicketResponse>>> getTickets(@Valid FilterTicketRequest filter) {
        List<Ticket> tickets = ticketService.getTickets(ticketRequestMapper.toFilterTicket(filter));
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponseList(tickets));
    }

    @Operation(
        summary = "Obtener ticket por ID", 
        description = "Retorna un ticket específico según su ID único. Incluye toda la información detallada del ticket, incluyendo su estado actual, asignado, solicitante, etc.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ticket encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ticket no encontrado con el ID proporcionado",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No se encontró un ticket con el ID: 123\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<TicketResponse>> getTicketById(
        @Parameter(description = "ID del ticket", required = true) @PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponse(ticket));
    }

    @Operation(
        summary = "Obtener historial de un ticket", 
        description = "Retorna el historial completo de un ticket específico, mostrando todos los cambios de estado, asignaciones, comentarios y acciones realizadas sobre el ticket ordenados cronológicamente.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Historial obtenido exitosamente",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketHistoryResponser.class)))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ticket no encontrado con el ID proporcionado",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No se encontró un ticket con el ID: 123\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        )
    })
    @GetMapping("/{id}/history")
    public ResponseEntity<CustomApiResponse<List<TicketHistoryResponser>>> getTicketCurrentHistory(
        @Parameter(description = "ID del ticket", required = true) @PathVariable Long id) {
        List<History> histories = ticketService.getTicketHistory(id);
        return ResponseBuilder.success(ticketResponseMapper.toHistoryResponseList(histories));
    }

    @Operation(
        summary = "Obtener estados de tickets", 
        description = "Retorna todos los posibles estados por los que puede pasar un ticket en su ciclo de vida. Útil para interfaces de usuario que necesitan mostrar los estados disponibles.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de estados obtenida exitosamente",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Status.class)))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        )
    })
    @GetMapping("/statuses")
    public ResponseEntity<CustomApiResponse<List<Status>>> getTicketStatuses() {
        List<Status> statuses = ticketService.getStatuses();
        return ResponseBuilder.success(statuses);
    }

    @Operation(
        summary = "Obtener tickets por solicitante", 
        description = "Retorna todos los tickets creados por un empleado específico identificado por su ID. Permite aplicar filtros adicionales a la búsqueda.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de tickets obtenida exitosamente",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketResponse.class)))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Empleado no encontrado con el ID proporcionado",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No se encontró un empleado con el ID: 123\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        )
    })
    @GetMapping("/requester/{employeeId}")
    public ResponseEntity<CustomApiResponse<List<TicketResponse>>> getTicketsByRequester(
        @Parameter(description = "ID del empleado solicitante", required = true) @PathVariable Long employeeId,
        @Parameter(description = "Filtros para los tickets") FilterTicketRequest filter) {
        Ticket.Filter filters = ticketRequestMapper.toFilterTicket(filter);
        List<Ticket> tickets = ticketService.getTicketsByRequester(filters, employeeId);
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponseList(tickets));
    }

    @Operation(
        summary = "Crear un nuevo ticket", 
        description = "Crea un nuevo ticket de incidente en el sistema. Requiere el ID del incidente relacionado, una descripción detallada y el ID del empleado que reporta el incidente.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ticket creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de ticket inválidos o incompletos",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"La descripción del ticket es obligatoria\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidente o empleado no encontrado con los IDs proporcionados",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No se encontró un incidente con el ID: 456\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Prohibido - No tiene el rol requerido para crear tickets",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No tiene permisos para crear tickets\"}")
            )
        )
    })
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLEADO_NO_TI')")
    public ResponseEntity<CustomApiResponse<TicketResponse>> createTicket(
        @Parameter(description = "Datos para crear el ticket", required = true)
        @RequestBody @Valid CreateTicketFormRequest request) {
        Ticket savedTicket = ticketService.createTicket(request.getIncidentId(), request.getDescription(),
                request.getEmployeeId());
        return ResponseBuilder.success(ticketResponseMapper.toTicketResponse(savedTicket));
    }

    @Operation(
        summary = "Ejecutar acción en un ticket", 
        description = "Ejecuta una acción específica sobre un ticket como cambio de estado, asignación a un técnico, escalamiento, cierre, etc. Cada acción puede requerir datos adicionales específicos en el campo updateValue y un comentario opcional.",
        tags = {"Tickets"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Acción ejecutada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"success\", \"message\": \"Acción ejecutada exitosamente\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de acción inválidos o incompletos",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"La acción solicitada requiere un valor de actualización\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ticket, empleado o acción no encontrada con los IDs proporcionados",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No se encontró un ticket con el ID: 123\"}")
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "No autorizado - Se requiere autenticación",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No autorizado\"}")
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Prohibido - No tiene los permisos necesarios para ejecutar esta acción",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": \"error\", \"message\": \"No tiene permisos para ejecutar esta acción\"}")
            )
        )
    })
    @PostMapping("/action")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLEADO_TI', 'ROLE_LIDER_EQUIPO_TI')")
    public ResponseEntity<CustomApiResponse<String>> executeAction(
        @Parameter(description = "Datos de la acción a ejecutar", required = true)
        @RequestBody @Valid ActionFormRequest request) {
        ticketService.executeAction(request.getEmployeeId(),
            request.getTicketId(),
            request.getActionId(),
            request.getUpdateValue(),
            request.getComment());

        return ResponseBuilder.success("Acción ejecutada exitosamente");
    }
    

}