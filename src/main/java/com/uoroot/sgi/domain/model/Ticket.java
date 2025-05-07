package com.uoroot.sgi.domain.model;

import java.time.LocalDateTime;
import java.util.List;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Filter {

        private boolean showNewTickets;
        private List<Integer> statusIds;
        private Integer assignedEmployeeId;
        private Integer ownerEmployeeId;
        private LocalDateTime dateFrom;
        private LocalDateTime dateTo;
        private Integer itTeamId;

    }

}
