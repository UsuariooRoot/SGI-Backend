package com.uoroot.sgi.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {

    private Long id;
    private Ticket ticket;
    private Employee employee;
    private Employee assignedEmployee;
    private Action action;
    private Status status;
    private Priority priority;
    private ITTeam team;
    private String comment;
    private LocalDateTime logged;

}
