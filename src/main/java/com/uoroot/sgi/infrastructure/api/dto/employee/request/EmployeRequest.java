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

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    @NotBlank(message = "El apellido paterno es requerido")
    @Size(max = 100, message = "El apellido paterno no puede tener más de 100 caracteres")
    private String paternalSurname;

    @Size(max = 100, message = "El apellido materno no puede tener más de 100 caracteres")
    private String maternalSurname;

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 150, message = "El correo electrónico no puede tener más de 150 caracteres")
    private String email;

    @NotNull(message = "El ID del rol es requerido")
    private Integer roleId;

    private Integer itTeamId;

}
