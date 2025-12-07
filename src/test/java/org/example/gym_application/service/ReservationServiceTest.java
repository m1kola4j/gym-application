package org.example.gym_application.service;

import org.example.gym_application.domain.*;
import org.example.gym_application.dto.ReservationDTO;
import org.example.gym_application.mapper.ReservationMapper;
import org.example.gym_application.repository.ClassSessionRepository;
import org.example.gym_application.repository.MemberRepository;
import org.example.gym_application.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ClassSessionRepository classSessionRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ClassSessionService classSessionService;

    @InjectMocks
    private ReservationService reservationService;

    private Member testMember;
    private ClassSession testClassSession;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@example.com")
                .build();

        Trainer trainer = Trainer.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .build();

        testClassSession = ClassSession.builder()
                .id(1L)
                .name("Joga")
                .startTime(LocalDateTime.now().plusDays(1))
                .maxParticipants(10)
                .trainer(trainer)
                .reservations(Set.of())
                .build();

        testReservation = Reservation.builder()
                .id(1L)
                .member(testMember)
                .classSession(testClassSession)
                .status(ReservationStatus.RESERVED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateReservation_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(classSessionRepository.findById(1L)).thenReturn(Optional.of(testClassSession));
        when(classSessionService.isAvailable(1L)).thenReturn(true);
        when(reservationRepository.existsByMemberIdAndClassSessionIdAndStatus(
                anyLong(), anyLong(), any(ReservationStatus.class))).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(ReservationDTO.builder()
                .id(1L)
                .memberId(1L)
                .classSessionId(1L)
                .status(ReservationStatus.RESERVED)
                .build());

        ReservationDTO result = reservationService.create(1L, 1L);

        assertNotNull(result);
        assertEquals(ReservationStatus.RESERVED, result.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_NoAvailableSpots() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(classSessionRepository.findById(1L)).thenReturn(Optional.of(testClassSession));
        when(classSessionService.isAvailable(1L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            reservationService.create(1L, 1L);
        });

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCancelReservation_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(ReservationDTO.builder()
                .id(1L)
                .status(ReservationStatus.CANCELLED)
                .build());

        ReservationDTO result = reservationService.cancel(1L);

        assertNotNull(result);
        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
}

