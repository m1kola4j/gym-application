package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.Member;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.mapper.MemberMapper;
import org.example.gym_application.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO findById(Long id) {
        return memberRepository.findById(id)
                .map(memberMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Członek o ID " + id + " nie został znaleziony"));
    }

    public MemberDTO create(MemberDTO dto) {
        validateMember(dto);
        checkEmailUnique(dto.getEmail(), null);
        
        Member member = memberMapper.toEntity(dto);
        member = memberRepository.save(member);
        return memberMapper.toDTO(member);
    }

    public MemberDTO update(Long id, MemberDTO dto) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Członek o ID " + id + " nie został znaleziony"));
        
        validateMember(dto);
        checkEmailUnique(dto.getEmail(), id);
        
        existingMember.setFirstName(dto.getFirstName());
        existingMember.setLastName(dto.getLastName());
        existingMember.setEmail(dto.getEmail());
        existingMember.setPhoneNumber(dto.getPhoneNumber());
        existingMember.setJoinDate(dto.getJoinDate());
        
        existingMember = memberRepository.save(existingMember);
        return memberMapper.toDTO(existingMember);
    }

    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("Członek o ID " + id + " nie został znaleziony");
        }
        memberRepository.deleteById(id);
    }

    private void validateMember(MemberDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email jest wymagany");
        }
        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Nieprawidłowy format email");
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().matches("^[0-9\\s\\-+()]+$")) {
            throw new IllegalArgumentException("Nieprawidłowy format numeru telefonu");
        }
        if (dto.getJoinDate() == null) {
            throw new IllegalArgumentException("Data dołączenia jest wymagana");
        }
    }

    private void checkEmailUnique(String email, Long excludeId) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            if (excludeId == null || !member.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Email " + email + " jest już używany");
            }
        });
    }
}

