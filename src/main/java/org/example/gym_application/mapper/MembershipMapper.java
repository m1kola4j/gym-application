package org.example.gym_application.mapper;

import org.example.gym_application.domain.Membership;
import org.example.gym_application.dto.MembershipDTO;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

    public MembershipDTO toDTO(Membership membership) {
        if (membership == null) {
            return null;
        }
        return MembershipDTO.builder()
                .id(membership.getId())
                .type(membership.getType())
                .startDate(membership.getStartDate())
                .endDate(membership.getEndDate())
                .price(membership.getPrice())
                .memberId(membership.getMember() != null ? membership.getMember().getId() : null)
                .memberName(membership.getMember() != null 
                        ? membership.getMember().getFirstName() + " " + membership.getMember().getLastName() 
                        : null)
                .build();
    }

    public Membership toEntity(MembershipDTO dto) {
        if (dto == null) {
            return null;
        }
        return Membership.builder()
                .id(dto.getId())
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .price(dto.getPrice())
                .build();
    }
}

