package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.Member;
import org.example.gym_application.domain.Role;
import org.example.gym_application.domain.User;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.repository.MemberRepository;
import org.example.gym_application.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String email, String password, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Użytkownik o nazwie " + username + " już istnieje");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Użytkownik o emailu " + email + " już istnieje");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    public User registerMember(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Sprawdź czy email już istnieje
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Użytkownik o emailu " + email + " już istnieje");
        }

        // Utwórz Member
        Member member = Member.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .joinDate(LocalDate.now())
                .build();
        member = memberRepository.save(member);

        // Utwórz User z powiązaniem do Member
        User user = User.builder()
                .username(email) // email jako username
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.MEMBER)
                .enabled(true)
                .memberId(member.getId())
                .build();

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie został znaleziony: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie został znaleziony: " + email));
    }
}

