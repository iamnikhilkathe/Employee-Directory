package com.SymphonyAI.employeedirectory.repository;

import com.SymphonyAI.employeedirectory.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
}
