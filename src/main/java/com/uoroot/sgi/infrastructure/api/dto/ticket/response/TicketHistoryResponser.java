package com.uoroot.sgi.infrastructure.api.dto.ticket.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryResponser {

    private Long id;
    private Employee employee;
    private Employee assignedEmployee;
    private Action action;
    private Status status;
    private Priority priority;
    private ITTeam team;
    private String comment;
    private LocalDateTime logged;

    @Data
    @Builder
    public static class Employee {
        private Long id;
        private String name;
        private String paternalSurname;
        private String maternalSurname;
        private String email;
    }

    public record Action(
        Integer id,
        String name
    ) {}
    
    public record Status(
        Integer id,
        String name
    ) {}

    public record Priority(
        Integer id,
        String name
    ) {}

    public record ITTeam(
        Integer id,
        String name
    ) {}

}
