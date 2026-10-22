package com.SymphonyAI.employeedirectory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Employee name is required.")
    private String name;

    @NotBlank(message = "Employee email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    private String designation;

    @NotNull(message = "Joining date is required.")
    private LocalDate joiningDate;

    @NotNull(message = "Department ID is required.")
    private Long departmentId;

    @Size(max = 10, message = "An employee can have at most 10 skills.")
    private Set<Long> skillIds;
}
