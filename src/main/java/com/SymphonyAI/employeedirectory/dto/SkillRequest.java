package com.SymphonyAI.employeedirectory.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillRequest(@NotBlank String name, String category) { }
