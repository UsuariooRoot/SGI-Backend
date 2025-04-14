package com.uoroot.sgi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    private Long id;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private Role role;
    private ITTeam itTeam;
    
}
