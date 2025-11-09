package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IslandRepository extends JpaRepository<Island, Long> {
    // Busca uma ilha e suas workstations
    @Query("SELECT i FROM Island i LEFT JOIN FETCH i.workstations WHERE i.id = :id")
    Optional<Island> findByIdWithWorkstations(Long id);
}