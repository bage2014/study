package com.familytree.service;

import com.familytree.model.Family;
import com.familytree.repository.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FamilyService {
    @Autowired
    private FamilyRepository familyRepository;
    
    public Family createFamily(String name, String description, String avatar, Long creatorId) {
        Family family = new Family();
        family.setName(name);
        family.setDescription(description);
        family.setAvatar(avatar);
        family.setCreatorId(creatorId);
        family.setCreatedAt(new Date());
        return familyRepository.save(family);
    }
    
    public List<Family> getFamiliesByCreatorId(Long creatorId) {
        return familyRepository.findByCreatorId(creatorId);
    }
    
    public Family getFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));
    }
    
    public Family updateFamily(Long familyId, String name, String description, String avatar) {
        Family family = getFamilyById(familyId);
        if (name != null) family.setName(name);
        if (description != null) family.setDescription(description);
        if (avatar != null) family.setAvatar(avatar);
        return familyRepository.save(family);
    }
    
    public void deleteFamily(Long familyId) {
        familyRepository.deleteById(familyId);
    }
}