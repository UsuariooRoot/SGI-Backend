package com.uoroot.sgi.domain.service;

import com.uoroot.sgi.domain.model.User;

public interface AuthService {

    User registerUser(String username, String password, Long employeeId);
    // User authenticateUser(String username, String password);
}
