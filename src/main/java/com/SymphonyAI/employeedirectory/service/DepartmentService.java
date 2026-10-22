package com.SymphonyAI.employeedirectory.service;

import com.SymphonyAI.employeedirectory.dto.DepartmentRequest;
import com.SymphonyAI.employeedirectory.dto.DepartmentResponse;
import com.SymphonyAI.employeedirectory.entity.DepartmentEntity;
import com.SymphonyAI.employeedirectory.exception.BusinessConflictException;
import com.SymphonyAI.employeedirectory.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.name().trim()))
            throw new BusinessConflictException("Department name already exists");
        DepartmentEntity department = new DepartmentEntity();
        department.setName(request.name().trim());
        department.setLocation(request.location());
        department.setHeadCount(0);
        return toResponse(departmentRepository.save(department));
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> departments(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse department(Long id) {
        return toResponse(findDepartment(id));
    }

    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        DepartmentEntity department = findDepartment(id);
        String name = request.name().trim();
        if (!department.getName().equalsIgnoreCase(name) && departmentRepository.existsByNameIgnoreCase(name))
            throw new BusinessConflictException("Department name already exists");
        department.setName(name);
        department.setLocation(request.location());
        return toResponse(department);
    }

    public void deleteDepartment(Long id) {
        DepartmentEntity department = findDepartment(id);
        if (department.getEmployees() != null && !department.getEmployees().isEmpty())
            throw new BusinessConflictException("Cannot delete a department with employees");
        departmentRepository.delete(department);
    }

    private DepartmentEntity findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department " + id + " not found"));
    }

    private DepartmentResponse toResponse(DepartmentEntity department) {
        int headCount = department.getEmployees() == null ? 0 : department.getEmployees().size();
        return new DepartmentResponse(department.getId(), department.getName(), department.getLocation(), headCount);
    }
}
