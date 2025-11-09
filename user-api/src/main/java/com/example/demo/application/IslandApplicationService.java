package com.example.demo.application;

import com.example.demo.domain.model.Island;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Workstation;
import com.example.demo.domain.repository.IslandRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.repository.WorkstationRepository;
import com.example.demo.domain.service.IslandDomainService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IslandApplicationService {

    private final IslandRepository islandRepository;
    private final UserRepository userRepository;
    private final WorkstationRepository workstationRepository;
    
    private final IslandDomainService islandDomainService;

    public IslandApplicationService(IslandRepository islandRepository,
                                    UserRepository userRepository,
                                    WorkstationRepository workstationRepository,
                                    IslandDomainService islandDomainService) { // Injeção de dependência
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
        this.workstationRepository = workstationRepository;
        this.islandDomainService = islandDomainService;
    }

    @Transactional
    public Workstation alocarWorkstationDisponivel(Long islandId, Long userId) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        Island island = islandRepository.findByIdWithWorkstations(islandId)
                .orElseThrow(() -> new RuntimeException("Ilha não encontrada: " + islandId));

        Workstation workstationAlocada = islandDomainService.alocarWorkstation(island, user);

        return workstationRepository.save(workstationAlocada);
    }
}