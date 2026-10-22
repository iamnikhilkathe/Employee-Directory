package com.SymphonyAI.employeedirectory.controller;

import com.SymphonyAI.employeedirectory.dto.*;
import com.SymphonyAI.employeedirectory.service.SkillService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "skills APIs", description = "skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> create(@Valid @RequestBody SkillRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(req));
    }

    @GetMapping
    @PageableAsQueryParam
    public ResponseEntity<Page<SkillResponse>> list(@SortDefault(sort = "name") @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(skillService.skills(pageable));
    }
}
