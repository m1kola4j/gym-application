package org.example.gym_application.service;

import org.example.gym_application.domain.Member;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.mapper.MemberMapper;
import org.example.gym_application.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    private MemberDTO testMemberDTO;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testMemberDTO = MemberDTO.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .phoneNumber("123456789")
                .joinDate(LocalDate.now())
                .build();

        testMember = Member.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .phoneNumber("123456789")
                .joinDate(LocalDate.now())
                .build();
    }

    @Test
    void testCreateMember_Success() {
        when(memberRepository.findByEmail(testMemberDTO.getEmail())).thenReturn(Optional.empty());
        when(memberMapper.toEntity(testMemberDTO)).thenReturn(testMember);
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        when(memberMapper.toDTO(testMember)).thenReturn(testMemberDTO);

        MemberDTO result = memberService.create(testMemberDTO);

        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testCreateMember_DuplicateEmail() {
        when(memberRepository.findByEmail(testMemberDTO.getEmail()))
                .thenReturn(Optional.of(testMember));

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.create(testMemberDTO);
        });

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMember_InvalidEmail() {
        testMemberDTO.setEmail("nieprawidlowy-email");

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.create(testMemberDTO);
        });

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testCreateMember_MissingFirstName() {
        testMemberDTO.setFirstName(null);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.create(testMemberDTO);
        });
    }
}

