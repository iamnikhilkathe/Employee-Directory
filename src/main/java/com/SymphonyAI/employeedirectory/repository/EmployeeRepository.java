package com.SymphonyAI.employeedirectory.repository;

import com.SymphonyAI.employeedirectory.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);

    @Query("select distinct e from EmployeeEntity e left join fetch e.department left join fetch e.skills where e.id = :id")
    java.util.Optional<EmployeeEntity> findDetailedById(@Param("id") Long id);

    @Query(value = "select e from EmployeeEntity e where (:departmentId is null or e.department.id = :departmentId)",
            countQuery = "select count(e) from EmployeeEntity e where (:departmentId is null or e.department.id = :departmentId)")
    Page<EmployeeEntity> findByDepartmentFilter(@Param("departmentId") Long departmentId, Pageable pageable);

    Page<EmployeeEntity> findByDepartment_Id(Long departmentId, Pageable pageable);
}
