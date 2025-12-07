package org.example.gym_application.mapper;

import org.example.gym_application.domain.Trainer;
import org.example.gym_application.dto.TrainerDTO;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public TrainerDTO toDTO(Trainer trainer) {
        if (trainer == null) {
            return null;
        }
        return TrainerDTO.builder()
                .id(trainer.getId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .specialization(trainer.getSpecialization())
                .description(trainer.getDescription())
                .build();
    }

    public Trainer toEntity(TrainerDTO dto) {
        if (dto == null) {
            return null;
        }
        return Trainer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .specialization(dto.getSpecialization())
                .description(dto.getDescription())
                .build();
    }
}

