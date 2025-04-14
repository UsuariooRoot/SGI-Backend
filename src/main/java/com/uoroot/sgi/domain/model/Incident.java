package com.uoroot.sgi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Incident {

    private Integer id;
    private String description;
    private Integer categoryId;
    private Integer priorityId;

}
