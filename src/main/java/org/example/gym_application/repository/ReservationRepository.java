package org.example.gym_application.repository;

import org.example.gym_application.domain.Reservation;
import org.example.gym_application.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByClassSessionId(Long classSessionId);
    boolean existsByMemberIdAndClassSessionIdAndStatus(Long memberId, Long classSessionId, ReservationStatus status);
}




