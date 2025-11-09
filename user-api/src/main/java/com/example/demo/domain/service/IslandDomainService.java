package com.example.demo.domain.service;

import com.example.demo.domain.model.Island;
import com.example.demo.domain.model.User;
import com.example.demo.domain.model.Workstation;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class IslandDomainService {

    public Workstation alocarWorkstation(Island island, User user) {
        
        Optional<Workstation> availableWorkstation = island.getWorkstations().stream()
                .filter(Workstation::isAvailable) 
                .findFirst();

        if (availableWorkstation.isEmpty()) {
            throw new RuntimeException("Nenhuma workstation disponível na ilha: " + island.getName());
        }

        Workstation workstation = availableWorkstation.get();
        
        workstation.setAssignedUser(user);

        return workstation;
    }
}