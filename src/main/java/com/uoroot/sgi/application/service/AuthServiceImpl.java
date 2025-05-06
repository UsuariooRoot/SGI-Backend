package com.uoroot.sgi.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uoroot.sgi.domain.model.User;
import com.uoroot.sgi.domain.model.Employee;
import com.uoroot.sgi.domain.repository.UserRepository;
import com.uoroot.sgi.domain.repository.EmployeeRepository;
import com.uoroot.sgi.domain.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public User registerUser(String username, String password, Long employeeId) {
        // Check if the employee already exists
        if (employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("El empleado no existe");
        }

        // Check if the user already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmployee(Employee.builder().id(employeeId).build());

        return userRepository.save(user);
    }
}
