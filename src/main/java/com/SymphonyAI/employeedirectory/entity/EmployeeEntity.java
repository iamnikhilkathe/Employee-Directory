package com.SymphonyAI.employeedirectory.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(
        name = "employee",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_email",
                        columnNames = "email"
                )
        }
)
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String designation;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departmentId", nullable = false)
    private DepartmentEntity department;

    @ManyToMany(fetch = FetchType.LAZY)
    @jakarta.persistence.JoinTable(name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"),
            uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"employee_id", "skill_id"}))
    private Set<SkillEntity>  skills;
}
