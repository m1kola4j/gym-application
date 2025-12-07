package org.example.gym_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gym_application.domain.MembershipType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDTO {
    private Long id;
    private MembershipType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Long memberId;
    private String memberName;
}

