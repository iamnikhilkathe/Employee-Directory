package com.SymphonyAI.employeedirectory.controller;

import com.SymphonyAI.employeedirectory.dto.*;
import com.SymphonyAI.employeedirectory.service.DepartmentService;
import com.SymphonyAI.employeedirectory.service.EmployeeService;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public DepartmentController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(req));
    }

    @GetMapping
    @PageableAsQueryParam
    public ResponseEntity<Page<DepartmentResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(departmentService.departments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.department(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest req) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<Page<EmployeeResponse>> employees(@PathVariable Long id,Pageable pageable) {
        return ResponseEntity.ok(employeeService.departmentEmployees(id, pageable));
    }
}
