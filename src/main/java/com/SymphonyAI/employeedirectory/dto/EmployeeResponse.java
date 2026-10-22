package com.SymphonyAI.employeedirectory.dto;

import java.time.LocalDate;
import java.util.Set;

public record EmployeeResponse(Long id, String name, String email, String designation,
        LocalDate joiningDate, DepartmentResponse department, Set<SkillResponse> skills) { }
