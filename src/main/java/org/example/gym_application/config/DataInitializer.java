package org.example.gym_application.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym_application.domain.Role;
import org.example.gym_application.domain.User;
import org.example.gym_application.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicjalizator danych testowych.
 * Tworzy konta ADMIN, STAFF i MEMBER przy starcie aplikacji (jeśli nie istnieją).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createTestUsers();
    }

    private void createTestUsers() {
        // ADMIN
        if (userRepository.findByEmail("admin@gym.pl").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gym.pl");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            log.info("Utworzono konto ADMIN: admin@gym.pl / admin123");
        }

        // STAFF
        if (userRepository.findByEmail("staff@gym.pl").isEmpty()) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setEmail("staff@gym.pl");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staff.setRole(Role.STAFF);
            staff.setEnabled(true);
            userRepository.save(staff);
            log.info("Utworzono konto STAFF: staff@gym.pl / staff123");
        }

        // MEMBER (testowy)
        if (userRepository.findByEmail("member@gym.pl").isEmpty()) {
            User member = new User();
            member.setUsername("member");
            member.setEmail("member@gym.pl");
            member.setPassword(passwordEncoder.encode("member123"));
            member.setRole(Role.MEMBER);
            member.setEnabled(true);
            userRepository.save(member);
            log.info("Utworzono konto MEMBER: member@gym.pl / member123");
        }
    }
}
