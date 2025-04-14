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
public class Ticket {

    private Long id;
    private Employee creator;
    private Employee owner;
    private Incident incident;
    private String description;
    private History currentHistory;
    private LocalDateTime created;

}
