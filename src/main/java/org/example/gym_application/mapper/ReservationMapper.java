package org.example.gym_application.mapper;

import org.example.gym_application.domain.Reservation;
import org.example.gym_application.dto.ReservationDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationDTO.builder()
                .id(reservation.getId())
                .memberId(reservation.getMember() != null ? reservation.getMember().getId() : null)
                .memberName(reservation.getMember() != null 
                        ? reservation.getMember().getFirstName() + " " + reservation.getMember().getLastName() 
                        : null)
                .classSessionId(reservation.getClassSession() != null ? reservation.getClassSession().getId() : null)
                .classSessionName(reservation.getClassSession() != null ? reservation.getClassSession().getName() : null)
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) {
            return null;
        }
        return Reservation.builder()
                .id(dto.getId())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}

