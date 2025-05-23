package com.uoroot.sgi.infrastructure.api.dto.employee.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Paternal surname is required")
    @Size(max = 100, message = "Paternal surname cannot be longer than 100 characters")
    private String paternalSurname;

    @Size(max = 100, message = "Maternal surname cannot be longer than 100 characters")
    private String maternalSurname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    @Size(max = 150, message = "Email cannot be longer than 150 characters")
    private String email;

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    private Integer itTeamId;

}
