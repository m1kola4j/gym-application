package org.example.gym_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String description;
}

