package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class IslandService {

    private final IslandRepository islandRepository;
    private final UserRepository userRepository;
    private final WorkstationRepository workstationRepository;

    public IslandService(IslandRepository islandRepository, 
                           UserRepository userRepository, 
                           WorkstationRepository workstationRepository) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
        this.workstationRepository = workstationRepository;
    }

    @Transactional
    public Workstation alocarWorkstationDisponivel(Long islandId, Long userId) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        Island island = islandRepository.findByIdWithWorkstations(islandId)
                .orElseThrow(() -> new RuntimeException("Ilha não encontrada: " + islandId));

        Optional<Workstation> availableWorkstation = island.getWorkstations().stream()
                .filter(Workstation::isAvailable)
                .findFirst();

        if (availableWorkstation.isEmpty()) {
            throw new RuntimeException("Nenhuma workstation disponível na ilha: " + island.getName());
        }

        Workstation workstation = availableWorkstation.get();
        workstation.setAssignedUser(user);
        
        return workstationRepository.save(workstation);
    }
}