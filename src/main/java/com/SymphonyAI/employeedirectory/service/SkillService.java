package com.SymphonyAI.employeedirectory.service;

import com.SymphonyAI.employeedirectory.dto.SkillRequest;
import com.SymphonyAI.employeedirectory.dto.SkillResponse;
import com.SymphonyAI.employeedirectory.entity.SkillEntity;
import com.SymphonyAI.employeedirectory.exception.BusinessConflictException;
import com.SymphonyAI.employeedirectory.repository.SkillRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Transactional
    public SkillResponse createSkill(SkillRequest req) {
        if (skillRepository.existsByNameIgnoreCase(req.name().trim())) throw new BusinessConflictException("Skill name already exists");
        SkillEntity s = new SkillEntity();
        s.setName(req.name().trim());
        s.setCategory(req.category());
        skillRepository.save(s);
        return new SkillResponse(s.getId(), s.getName(), s.getCategory());
    }


    public Page<SkillResponse> skills(Pageable pageable) {
        return skillRepository.findAll(pageable).map(SkillResponse::skill);
    }



}
