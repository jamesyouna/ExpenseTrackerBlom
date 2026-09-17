package com.BLOM.expensetrackerapi.config;

import com.BLOM.expensetrackerapi.security.CustomUserDetailsService;
import com.BLOM.expensetrackerapi.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtRequestFilter authenticationJwtTokenFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean //create this object ,manage it, make it available for the rest of application
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for REST API / H2 console
                .csrf(csrf -> csrf.disable())

                // H2 console uses frames
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // Endpoint permissions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/register-page",
                                "/resend-verification",

                                "/verify-email",
                                "/verify-page",

                                "/login-page",
                                "/dashboard",
                                "/expenses-page",
                                "/categories-page",
                                "/profile-page",
                                "/admin-page",
                                "/logistics-page",


                                "/h2-console/**",

                                "/css/**",
                                "/js/**",
                                "/img/**"
                        ).permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // JWT = no server-side login session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .userDetailsService(userDetailsService);

        // Check JWT before Spring's normal username/password filter
        http.addFilterBefore(
                authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}