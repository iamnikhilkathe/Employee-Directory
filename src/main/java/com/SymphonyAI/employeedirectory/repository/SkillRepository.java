package com.SymphonyAI.employeedirectory.repository;

import com.SymphonyAI.employeedirectory.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<SkillEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
}
