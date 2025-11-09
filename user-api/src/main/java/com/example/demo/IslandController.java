package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/islands")
public class IslandController {

    private final IslandService islandService;

    public IslandController(IslandService islandService) {
        this.islandService = islandService;
    }

    public static class AllocationRequest {
        private Long userId;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    @PostMapping("/{islandId}/allocate")
    public ResponseEntity<Workstation> allocateWorkstation(
            @PathVariable Long islandId,
            @RequestBody AllocationRequest request) {
        try {
            Workstation allocatedWorkstation = islandService.alocarWorkstationDisponivel(islandId, request.getUserId());
            // Retorna a workstation alocada
            return ResponseEntity.ok(allocatedWorkstation);
        } catch (RuntimeException e) {
            
            return ResponseEntity.badRequest().body(null);
        }
    }
}