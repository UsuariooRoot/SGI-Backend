package com.uoroot.sgi.infrastructure.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));
                corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.setAllowedHeaders(List.of("*"));
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth
                    // Public routes for authentication
                    .requestMatchers("/api/auth/**").permitAll()
                    // Public paths for Swagger/OpenAPI documentation
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    // Routes for specific endpoints requiring authentication
                    .requestMatchers(HttpMethod.GET, "/api/tickets").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers(HttpMethod.GET, "/api/tickets/**").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers(HttpMethod.GET, "/api/incidents").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers(HttpMethod.GET, "/api/incidents/**").hasAnyAuthority("ROLE_EMPLEADO_TI", "ROLE_LIDER_EQUIPO_TI", "ROLE_EMPLEADO_NO_TI")
                    .requestMatchers("/api/incidents/categorized").permitAll()
                    .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
