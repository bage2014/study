package com.familytree.controller;

import com.familytree.model.Family;
import com.familytree.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
public class FamilyController {
    @Autowired
    private FamilyService familyService;
    
    @PostMapping
    public ResponseEntity<Family> createFamily(@RequestAttribute("userId") Long userId, @RequestBody Family family) {
        Family createdFamily = familyService.createFamily(
                family.getName(),
                family.getDescription(),
                family.getAvatar(),
                userId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFamily);
    }
    
    @GetMapping
    public ResponseEntity<List<Family>> getFamilies(@RequestAttribute("userId") Long userId) {
        List<Family> families = familyService.getFamiliesByCreatorId(userId);
        return ResponseEntity.ok(families);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Family> getFamily(@PathVariable Long id) {
        Family family = familyService.getFamilyById(id);
        return ResponseEntity.ok(family);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Family> updateFamily(@PathVariable Long id, @RequestBody Family family) {
        Family updatedFamily = familyService.updateFamily(
                id,
                family.getName(),
                family.getDescription(),
                family.getAvatar()
        );
        return ResponseEntity.ok(updatedFamily);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }
}