package org.example.gym_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSessionDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private Integer maxParticipants;
    private Long trainerId;
    private String trainerName;
    private Integer currentReservations;
}

