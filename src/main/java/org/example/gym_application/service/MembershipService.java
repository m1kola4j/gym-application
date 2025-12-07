package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.Member;
import org.example.gym_application.domain.Membership;
import org.example.gym_application.dto.MembershipDTO;
import org.example.gym_application.mapper.MembershipMapper;
import org.example.gym_application.repository.MemberRepository;
import org.example.gym_application.repository.MembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipMapper membershipMapper;

    public List<MembershipDTO> findAll() {
        return membershipRepository.findAll().stream()
                .map(membershipMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MembershipDTO findById(Long id) {
        return membershipRepository.findById(id)
                .map(membershipMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Karnet o ID " + id + " nie został znaleziony"));
    }

    public MembershipDTO create(MembershipDTO dto) {
        validateMembership(dto);
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Członek o ID " + dto.getMemberId() + " nie został znaleziony"));
        
        Membership membership = membershipMapper.toEntity(dto);
        membership.setMember(member);
        membership = membershipRepository.save(membership);
        return membershipMapper.toDTO(membership);
    }

    public MembershipDTO update(Long id, MembershipDTO dto) {
        Membership existingMembership = membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Karnet o ID " + id + " nie został znaleziony"));
        
        validateMembership(dto);
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Członek o ID " + dto.getMemberId() + " nie został znaleziony"));
        
        existingMembership.setType(dto.getType());
        existingMembership.setStartDate(dto.getStartDate());
        existingMembership.setEndDate(dto.getEndDate());
        existingMembership.setPrice(dto.getPrice());
        existingMembership.setMember(member);
        
        existingMembership = membershipRepository.save(existingMembership);
        return membershipMapper.toDTO(existingMembership);
    }

    public void delete(Long id) {
        if (!membershipRepository.existsById(id)) {
            throw new IllegalArgumentException("Karnet o ID " + id + " nie został znaleziony");
        }
        membershipRepository.deleteById(id);
    }

    public List<MembershipDTO> findByMemberId(Long memberId) {
        return membershipRepository.findByMemberId(memberId).stream()
                .map(membershipMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean isActive(Long memberId) {
        LocalDate today = LocalDate.now();
        return membershipRepository.existsByMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                memberId, today, today);
    }

    private void validateMembership(MembershipDTO dto) {
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Typ karnetu jest wymagany");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Data rozpoczęcia jest wymagana");
        }
        if (dto.getEndDate() == null) {
            throw new IllegalArgumentException("Data zakończenia jest wymagana");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Data zakończenia nie może być wcześniejsza niż data rozpoczęcia");
        }
        if (dto.getPrice() == null || dto.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Cena musi być większa lub równa 0");
        }
        if (dto.getMemberId() == null) {
            throw new IllegalArgumentException("Członek jest wymagany");
        }
    }
}

