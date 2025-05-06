package com.uoroot.sgi.infrastructure.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uoroot.sgi.domain.model.User;
import com.uoroot.sgi.domain.model.Employee;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private Long employeeId;
    private String role;
    private Integer itTeam;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        Employee employee = user.getEmployee();
        
        // Obtener los permisos del rol del empleado
        // List<GrantedAuthority> authorities = employee.getRole().getPermissions().stream()
        //         .map(permission -> new SimpleGrantedAuthority(permission.getName()))
        //         .collect(Collectors.toList());
        
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole().getName()));
        
        // Añadir el rol como autoridad
        // authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.getRole().getName()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                employee.getId(),
                employee.getRole().getName(),
                employee.getItTeam() != null ? employee.getItTeam().getId() : null,
                user.isEnabled(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getRole() {
        return role;
    }

    public Integer getItTeam() {
        return itTeam;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
