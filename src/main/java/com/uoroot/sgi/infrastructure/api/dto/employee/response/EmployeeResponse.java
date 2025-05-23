package com.uoroot.sgi.infrastructure.api.dto.employee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private Role role;
    private ITTeam itTeam;

    public static record Role(
            Long id,
            String name) {
    }

    public static record ITTeam(
            Long id,
            String name) {
    }
}
