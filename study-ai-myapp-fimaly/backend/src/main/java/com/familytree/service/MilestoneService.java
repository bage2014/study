package com.familytree.service;

import com.familytree.model.Milestone;
import com.familytree.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MilestoneService {
    @Autowired
    private MilestoneRepository milestoneRepository;

    public List<Milestone> getMilestonesByMemberId(Long memberId) {
        return milestoneRepository.findByMemberId(memberId);
    }

    public List<Milestone> getPublicMilestonesByMemberId(Long memberId) {
        return milestoneRepository.findByMemberIdAndIsPublicTrue(memberId);
    }

    public Optional<Milestone> getMilestoneById(Long id) {
        return milestoneRepository.findById(id);
    }

    public Milestone createMilestone(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    public Milestone updateMilestone(Long id, Milestone milestoneDetails) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        milestone.setTitle(milestoneDetails.getTitle());
        milestone.setContent(milestoneDetails.getContent());
        milestone.setPublic(milestoneDetails.isPublic());

        return milestoneRepository.save(milestone);
    }

    public void deleteMilestone(Long id) {
        milestoneRepository.deleteById(id);
    }
}
