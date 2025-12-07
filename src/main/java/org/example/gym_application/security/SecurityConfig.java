package org.example.gym_application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Tymczasowa konfiguracja bezpieczeństwa – wszystkie żądania są dozwolone.
 * W kolejnych gałęziach zostanie zastąpiona pełnym mechanizmem ról/JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        // httpBasic() wyłączone - logowanie będzie w osobnym branchu feature/security-auth
        return http.build();
    }
}



