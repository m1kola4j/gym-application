package org.example.gym_application.mapper;

import org.example.gym_application.domain.ClassSession;
import org.example.gym_application.dto.ClassSessionDTO;
import org.springframework.stereotype.Component;

@Component
public class ClassSessionMapper {

    public ClassSessionDTO toDTO(ClassSession session) {
        if (session == null) {
            return null;
        }
        return ClassSessionDTO.builder()
                .id(session.getId())
                .name(session.getName())
                .description(session.getDescription())
                .startTime(session.getStartTime())
                .maxParticipants(session.getMaxParticipants())
                .trainerId(session.getTrainer() != null ? session.getTrainer().getId() : null)
                .trainerName(session.getTrainer() != null 
                        ? session.getTrainer().getFirstName() + " " + session.getTrainer().getLastName() 
                        : null)
                .currentReservations(session.getReservations() != null 
                        ? (int) session.getReservations().stream()
                                .filter(r -> r.getStatus() == org.example.gym_application.domain.ReservationStatus.RESERVED)
                                .count()
                        : 0)
                .build();
    }

    public ClassSession toEntity(ClassSessionDTO dto) {
        if (dto == null) {
            return null;
        }
        return ClassSession.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .startTime(dto.getStartTime())
                .maxParticipants(dto.getMaxParticipants())
                .build();
    }
}

