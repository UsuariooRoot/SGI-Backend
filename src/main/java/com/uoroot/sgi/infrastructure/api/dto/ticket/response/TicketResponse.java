package com.uoroot.sgi.infrastructure.api.dto.ticket.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {

    private Long id;
    private Employee creator;
    private Employee owner;
    private Incident incident;
    private String description;
    private Long currentHistoryId;
    private Employee assignedEmployee;
    private Status status;
    private Priority priority;
    private ITTeam itTeam;
    private LocalDateTime created;

    @Data
    @Builder
    public static class Employee {

        private Long id;
        private String name;
        private String paternalSurname;
        private String maternalSurname;
        private String email;

    }

    @Data
    @Builder
    public static class Incident {

        private Integer id;
        private String description;
        private Integer categoryId;

    }

    public record Status(
            Integer id,
            String name) {
    }

    public record Priority(
            Integer id,
            String name) {
    }

    public record ITTeam(
            Integer id,
            String name) {
    }

}
