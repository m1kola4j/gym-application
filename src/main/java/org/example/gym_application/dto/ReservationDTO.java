package org.example.gym_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gym_application.domain.ReservationStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private Long classSessionId;
    private String classSessionName;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}

