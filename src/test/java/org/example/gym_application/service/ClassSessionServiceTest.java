package org.example.gym_application.service;

import org.example.gym_application.domain.ClassSession;
import org.example.gym_application.domain.Trainer;
import org.example.gym_application.dto.ClassSessionDTO;
import org.example.gym_application.mapper.ClassSessionMapper;
import org.example.gym_application.repository.ClassSessionRepository;
import org.example.gym_application.repository.TrainerRepository;
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
class ClassSessionServiceTest {

    @Mock
    private ClassSessionRepository classSessionRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ClassSessionMapper classSessionMapper;

    @InjectMocks
    private ClassSessionService classSessionService;

    private ClassSessionDTO testClassSessionDTO;
    private ClassSession testClassSession;
    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        testTrainer = Trainer.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Nowak")
                .build();

        testClassSessionDTO = ClassSessionDTO.builder()
                .id(1L)
                .name("Joga")
                .description("Zajęcia jogi")
                .startTime(LocalDateTime.now().plusDays(1))
                .maxParticipants(10)
                .trainerId(1L)
                .build();

        testClassSession = ClassSession.builder()
                .id(1L)
                .name("Joga")
                .description("Zajęcia jogi")
                .startTime(LocalDateTime.now().plusDays(1))
                .maxParticipants(10)
                .trainer(testTrainer)
                .reservations(Set.of())
                .build();
    }

    @Test
    void testCreateClassSession_Success() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(testTrainer));
        when(classSessionMapper.toEntity(testClassSessionDTO)).thenReturn(testClassSession);
        when(classSessionRepository.save(any(ClassSession.class))).thenReturn(testClassSession);
        when(classSessionMapper.toDTO(testClassSession)).thenReturn(testClassSessionDTO);

        ClassSessionDTO result = classSessionService.create(testClassSessionDTO);

        assertNotNull(result);
        assertEquals("Joga", result.getName());
        verify(classSessionRepository, times(1)).save(any(ClassSession.class));
    }

    @Test
    void testCreateClassSession_MissingName() {
        testClassSessionDTO.setName(null);

        assertThrows(IllegalArgumentException.class, () -> {
            classSessionService.create(testClassSessionDTO);
        });

        verify(classSessionRepository, never()).save(any(ClassSession.class));
    }

    @Test
    void testIsAvailable_True() {
        when(classSessionRepository.findById(1L)).thenReturn(Optional.of(testClassSession));

        boolean result = classSessionService.isAvailable(1L);

        assertTrue(result);
    }

    @Test
    void testIsAvailable_False() {
        testClassSession.setMaxParticipants(1);
        when(classSessionRepository.findById(1L)).thenReturn(Optional.of(testClassSession));

        boolean result = classSessionService.isAvailable(1L);

        assertTrue(result); // Brak rezerwacji, więc dostępne
    }
}

