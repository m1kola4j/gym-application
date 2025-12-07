package org.example.gym_application.service;

import lombok.RequiredArgsConstructor;
import org.example.gym_application.domain.Trainer;
import org.example.gym_application.dto.TrainerDTO;
import org.example.gym_application.mapper.TrainerMapper;
import org.example.gym_application.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    public List<TrainerDTO> findAll() {
        return trainerRepository.findAll().stream()
                .map(trainerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrainerDTO findById(Long id) {
        return trainerRepository.findById(id)
                .map(trainerMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Trener o ID " + id + " nie został znaleziony"));
    }

    public TrainerDTO create(TrainerDTO dto) {
        validateTrainer(dto);
        Trainer trainer = trainerMapper.toEntity(dto);
        trainer = trainerRepository.save(trainer);
        return trainerMapper.toDTO(trainer);
    }

    public TrainerDTO update(Long id, TrainerDTO dto) {
        Trainer existingTrainer = trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trener o ID " + id + " nie został znaleziony"));
        
        validateTrainer(dto);
        
        existingTrainer.setFirstName(dto.getFirstName());
        existingTrainer.setLastName(dto.getLastName());
        existingTrainer.setSpecialization(dto.getSpecialization());
        existingTrainer.setDescription(dto.getDescription());
        
        existingTrainer = trainerRepository.save(existingTrainer);
        return trainerMapper.toDTO(existingTrainer);
    }

    public void delete(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trener o ID " + id + " nie został znaleziony"));
        
        if (!trainer.getClassSessions().isEmpty()) {
            throw new IllegalStateException("Nie można usunąć trenera, który ma przypisane zajęcia");
        }
        
        trainerRepository.deleteById(id);
    }

    private void validateTrainer(TrainerDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Imię jest wymagane");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko jest wymagane");
        }
    }
}

