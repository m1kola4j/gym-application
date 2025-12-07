package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.ClassSession;
import org.example.gym_application.domain.Trainer;
import org.example.gym_application.dto.ClassSessionDTO;
import org.example.gym_application.mapper.ClassSessionMapper;
import org.example.gym_application.repository.ClassSessionRepository;
import org.example.gym_application.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final TrainerRepository trainerRepository;
    private final ClassSessionMapper classSessionMapper;

    public List<ClassSessionDTO> findAll() {
        return classSessionRepository.findAll().stream()
                .map(classSessionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClassSessionDTO findById(Long id) {
        return classSessionRepository.findById(id)
                .map(classSessionMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Zajęcia o ID " + id + " nie zostały znalezione"));
    }

    public ClassSessionDTO create(ClassSessionDTO dto) {
        validateClassSession(dto);
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException("Trener o ID " + dto.getTrainerId() + " nie został znaleziony"));
        
        ClassSession session = classSessionMapper.toEntity(dto);
        session.setTrainer(trainer);
        session = classSessionRepository.save(session);
        return classSessionMapper.toDTO(session);
    }

    public ClassSessionDTO update(Long id, ClassSessionDTO dto) {
        ClassSession existingSession = classSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zajęcia o ID " + id + " nie zostały znalezione"));
        
        validateClassSession(dto);
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException("Trener o ID " + dto.getTrainerId() + " nie został znaleziony"));
        
        existingSession.setName(dto.getName());
        existingSession.setDescription(dto.getDescription());
        existingSession.setStartTime(dto.getStartTime());
        existingSession.setMaxParticipants(dto.getMaxParticipants());
        existingSession.setTrainer(trainer);
        
        existingSession = classSessionRepository.save(existingSession);
        return classSessionMapper.toDTO(existingSession);
    }

    public void delete(Long id) {
        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zajęcia o ID " + id + " nie zostały znalezione"));
        
        // Sprawdź czy są aktywne rezerwacje (tylko RESERVED, nie CANCELLED)
        long activeReservations = session.getReservations().stream()
                .filter(r -> r.getStatus() == org.example.gym_application.domain.ReservationStatus.RESERVED)
                .count();
        
        if (activeReservations > 0) {
            throw new IllegalStateException("Nie można usunąć zajęć, które mają aktywne rezerwacje. Najpierw anuluj rezerwacje.");
        }
        
        // Usuwanie zajęć automatycznie usunie też powiązane rezerwacje (cascade)
        classSessionRepository.deleteById(id);
    }

    public boolean isAvailable(Long classSessionId) {
        ClassSession session = classSessionRepository.findById(classSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Zajęcia o ID " + classSessionId + " nie zostały znalezione"));
        
        long activeReservations = session.getReservations().stream()
                .filter(r -> r.getStatus() == org.example.gym_application.domain.ReservationStatus.RESERVED)
                .count();
        
        return activeReservations < session.getMaxParticipants();
    }

    private void validateClassSession(ClassSessionDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa zajęć jest wymagana");
        }
        if (dto.getStartTime() == null) {
            throw new IllegalArgumentException("Data i godzina rozpoczęcia są wymagane");
        }
        if (dto.getMaxParticipants() == null || dto.getMaxParticipants() <= 0) {
            throw new IllegalArgumentException("Maksymalna liczba uczestników musi być większa od 0");
        }
        if (dto.getTrainerId() == null) {
            throw new IllegalArgumentException("Trener jest wymagany");
        }
    }
}

