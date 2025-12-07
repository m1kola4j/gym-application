package org.example.gym_application.service;

import org.example.gym_application.domain.Member;
import org.example.gym_application.domain.Membership;
import org.example.gym_application.domain.MembershipType;
import org.example.gym_application.dto.MembershipDTO;
import org.example.gym_application.mapper.MembershipMapper;
import org.example.gym_application.repository.MemberRepository;
import org.example.gym_application.repository.MembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipMapper membershipMapper;

    @InjectMocks
    private MembershipService membershipService;

    private MembershipDTO testMembershipDTO;
    private Membership testMembership;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        testMembershipDTO = MembershipDTO.builder()
                .id(1L)
                .type(MembershipType.MONTHLY)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .price(new BigDecimal("100.00"))
                .memberId(1L)
                .build();

        testMembership = Membership.builder()
                .id(1L)
                .type(MembershipType.MONTHLY)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .price(new BigDecimal("100.00"))
                .member(testMember)
                .build();
    }

    @Test
    void testCreateMembership_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(membershipMapper.toEntity(testMembershipDTO)).thenReturn(testMembership);
        when(membershipRepository.save(any(Membership.class))).thenReturn(testMembership);
        when(membershipMapper.toDTO(testMembership)).thenReturn(testMembershipDTO);

        MembershipDTO result = membershipService.create(testMembershipDTO);

        assertNotNull(result);
        assertEquals(MembershipType.MONTHLY, result.getType());
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    void testCreateMembership_InvalidDateRange() {
        testMembershipDTO.setEndDate(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> {
            membershipService.create(testMembershipDTO);
        });

        verify(membershipRepository, never()).save(any(Membership.class));
    }

    @Test
    void testCreateMembership_NegativePrice() {
        testMembershipDTO.setPrice(new BigDecimal("-10.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            membershipService.create(testMembershipDTO);
        });
    }

    @Test
    void testIsActive_True() {
        LocalDate today = LocalDate.now();
        when(membershipRepository.existsByMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                1L, today, today)).thenReturn(true);

        boolean result = membershipService.isActive(1L);

        assertTrue(result);
    }
}

