package org.example.gym_application.mapper;

import org.example.gym_application.domain.Member;
import org.example.gym_application.dto.MemberDTO;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDTO toDTO(Member member) {
        if (member == null) {
            return null;
        }
        return MemberDTO.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .joinDate(member.getJoinDate())
                .build();
    }

    public Member toEntity(MemberDTO dto) {
        if (dto == null) {
            return null;
        }
        return Member.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .joinDate(dto.getJoinDate())
                .build();
    }
}

