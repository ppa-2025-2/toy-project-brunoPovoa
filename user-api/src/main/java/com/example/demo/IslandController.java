package com.example.demo;

import com.example.demo.application.IslandApplicationService;
import com.example.demo.domain.model.Workstation; 

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/islands")
public class IslandController {

    private final IslandApplicationService islandService;

    public IslandController(IslandApplicationService islandService) {
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
            return ResponseEntity.ok(allocatedWorkstation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); 
        }
    }
}