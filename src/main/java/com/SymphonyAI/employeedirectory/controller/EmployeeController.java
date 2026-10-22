package com.SymphonyAI.employeedirectory.controller;

import com.SymphonyAI.employeedirectory.dto.*;
import com.SymphonyAI.employeedirectory.service.EmployeeService;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(req));
    }

    @GetMapping
    @PageableAsQueryParam
    public ResponseEntity<Page<EmployeeResponse>> list(@RequestParam(required = false) Long departmentId, Pageable pageable) {
        return ResponseEntity.ok(employeeService.employees(departmentId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.employee(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest req) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/skills/{sid}")
    public ResponseEntity<EmployeeResponse> addSkill(@PathVariable Long id, @PathVariable Long sid) {
        return ResponseEntity.ok(employeeService.addSkill(id, sid));
    }

    @DeleteMapping("/{id}/skills/{sid}")
    public ResponseEntity<EmployeeResponse> removeSkill(@PathVariable Long id, @PathVariable Long sid) {
        return ResponseEntity.ok(employeeService.removeSkill(id, sid));
    }
}
