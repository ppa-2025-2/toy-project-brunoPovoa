package com.example.demo.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Island {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "island",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Workstation> workstations = new ArrayList<>();

    // Construtores, Getters e Setters

    public Island() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Workstation> getWorkstations() {
        return workstations;
    }

    public void setWorkstations(List<Workstation> workstations) {
        this.workstations = workstations;
    }
    
    public void addWorkstation(Workstation workstation) {
        workstations.add(workstation);
        workstation.setIsland(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return Objects.equals(id, island.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Workstation alocarWorkstation(User user) {
        
        Optional<Workstation> availableWorkstation = this.getWorkstations().stream()
                .filter(Workstation::isAvailable)
                .findFirst();

        if (availableWorkstation.isEmpty()) {
            throw new RuntimeException("Nenhuma workstation disponível na ilha: " + this.getName());
        }

        Workstation workstation = availableWorkstation.get();
        
        workstation.assignUser(user);

        return workstation;
    }
}