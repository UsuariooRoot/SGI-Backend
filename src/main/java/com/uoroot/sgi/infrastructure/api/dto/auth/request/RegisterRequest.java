package com.uoroot.sgi.infrastructure.api.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String username;

    private String password;

    private Long employeeId;

}
