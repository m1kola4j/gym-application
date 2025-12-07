package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.ClassSession;
import org.example.gym_application.domain.Member;
import org.example.gym_application.domain.Reservation;
import org.example.gym_application.domain.ReservationStatus;
import org.example.gym_application.dto.ReservationDTO;
import org.example.gym_application.mapper.ReservationMapper;
import org.example.gym_application.repository.ClassSessionRepository;
import org.example.gym_application.repository.MemberRepository;
import org.example.gym_application.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ClassSessionRepository classSessionRepository;
    private final ReservationMapper reservationMapper;
    private final ClassSessionService classSessionService;

    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO findById(Long id) {
        return reservationRepository.findById(id)
                .map(reservationMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Rezerwacja o ID " + id + " nie została znaleziona"));
    }

    public ReservationDTO create(Long memberId, Long classSessionId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Członek o ID " + memberId + " nie został znaleziony"));
        
        ClassSession classSession = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Zajęcia o ID " + classSessionId + " nie zostały znalezione"));
        
        // Sprawdź czy są jeszcze wolne miejsca
        if (!classSessionService.isAvailable(classSessionId)) {
            throw new IllegalStateException("Brak wolnych miejsc na te zajęcia");
        }
        
        // Sprawdź czy członek już nie ma rezerwacji na te zajęcia
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndClassSessionIdAndStatus(
                memberId, classSessionId, ReservationStatus.RESERVED);
        if (alreadyReserved) {
            throw new IllegalStateException("Członek ma już rezerwację na te zajęcia");
        }
        
        Reservation reservation = Reservation.builder()
                .member(member)
                .classSession(classSession)
                .status(ReservationStatus.RESERVED)
                .createdAt(LocalDateTime.now())
                .build();
        
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(reservation);
    }

    public ReservationDTO cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rezerwacja o ID " + id + " nie została znaleziona"));
        
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalStateException("Można anulować tylko aktywne rezerwacje");
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(reservation);
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Rezerwacja o ID " + id + " nie została znaleziona");
        }
        reservationRepository.deleteById(id);
    }

    public List<ReservationDTO> findByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId).stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findByClassSessionId(Long classSessionId) {
        return reservationRepository.findByClassSessionId(classSessionId).stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }
}

