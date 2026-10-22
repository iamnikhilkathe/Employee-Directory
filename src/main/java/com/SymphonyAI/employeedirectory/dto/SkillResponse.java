package com.SymphonyAI.employeedirectory.dto;

import com.SymphonyAI.employeedirectory.entity.SkillEntity;

public record SkillResponse(Long id, String name, String category) {

    public static SkillResponse skill(SkillEntity s) {
        return new SkillResponse(s.getId(), s.getName(), s.getCategory());
    }
}
