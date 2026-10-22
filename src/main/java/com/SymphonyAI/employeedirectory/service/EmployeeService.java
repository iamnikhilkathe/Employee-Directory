package com.SymphonyAI.employeedirectory.service;

import com.SymphonyAI.employeedirectory.dto.DepartmentResponse;
import com.SymphonyAI.employeedirectory.dto.EmployeeRequest;
import com.SymphonyAI.employeedirectory.dto.EmployeeResponse;
import com.SymphonyAI.employeedirectory.dto.SkillResponse;
import com.SymphonyAI.employeedirectory.entity.DepartmentEntity;
import com.SymphonyAI.employeedirectory.entity.EmployeeEntity;
import com.SymphonyAI.employeedirectory.entity.SkillEntity;
import com.SymphonyAI.employeedirectory.exception.BusinessConflictException;
import com.SymphonyAI.employeedirectory.repository.DepartmentRepository;
import com.SymphonyAI.employeedirectory.repository.EmployeeRepository;
import com.SymphonyAI.employeedirectory.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.SymphonyAI.employeedirectory.exception.ExceptionUtil.notFound;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final SkillRepository skillRepository;
    private final int maxSkills;
    private final int defaultPageSize;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                           SkillRepository skillRepository,
                           @Value("${directory.max-skills-per-employee}") int maxSkills,
                           @Value("${pagination.default-page-size}") int defaultPageSize) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.skillRepository = skillRepository;
        this.maxSkills = maxSkills;
        this.defaultPageSize = defaultPageSize;
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmailIgnoreCase(request.getEmail().trim())) throw new BusinessConflictException("Email already exists");
        EmployeeEntity employee = new EmployeeEntity();
        apply(employee, request);
        return toResponse(employeeRepository.save(employee));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse employee(Long id) {
        return toResponse(findEmployee(id));
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> employees(Long departmentId,Pageable pageable) {
        return employeeRepository.findByDepartmentFilter(departmentId,pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> departmentEmployees(Long departmentId,Pageable pageable) {
        departmentRepository.findById(departmentId) .orElseThrow(() -> notFound("Department", departmentId));
        return employeeRepository.findByDepartment_Id(departmentId,pageable).map(this::toResponse);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        EmployeeEntity employee = findEmployee(id);
        if (!employee.getEmail().equalsIgnoreCase(request.getEmail().trim())
                && employeeRepository.existsByEmailIgnoreCase(request.getEmail().trim()))
            throw new BusinessConflictException("Email already exists");
        apply(employee, request);
        return toResponse(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.delete(employeeRepository.findById(id)
                .orElseThrow(() -> notFound("Employee", id)));
    }

    public EmployeeResponse addSkill(Long employeeId, Long skillId) {
        EmployeeEntity employee = findEmployee(employeeId);
        SkillEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> notFound("Skill", skillId));
        if (!employee.getSkills().contains(skill) && employee.getSkills().size() >= maxSkills)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Employee cannot have more than " + maxSkills + " skills");
        employee.getSkills().add(skill);
        return toResponse(employee);
    }

    public EmployeeResponse removeSkill(Long employeeId, Long skillId) {
        EmployeeEntity employee = findEmployee(employeeId);
        SkillEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> notFound("Skill", skillId));
        employee.getSkills().remove(skill);
        return toResponse(employee);
    }

    private void apply(EmployeeEntity employee, EmployeeRequest request) {
        employee.setName(request.getName().trim());
        employee.setEmail(request.getEmail().trim());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartment(departmentRepository.findById(request.getDepartmentId()) .orElseThrow(() -> notFound("Department", request.getDepartmentId())));
        Set<Long> skillIds = request.getSkillIds() == null ? Set.of() : request.getSkillIds();
        if (skillIds.size() > maxSkills)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Employee cannot have more than " + maxSkills + " skills");
        Set<SkillEntity> selected = new HashSet<>(skillRepository.findAllById(skillIds));
        if (selected.size() != skillIds.size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more skillIds do not exist");
        employee.setSkills(selected);
    }

    private EmployeeEntity findEmployee(Long id) {
        return employeeRepository.findDetailedById(id).orElseThrow(() -> notFound("Employee", id));
    }

    private void validatePage(int page, int size) {
        if (page < 0 || size < 1 || size > 200)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page must be non-negative and size must be between 1 and 200");
    }

    private EmployeeResponse toResponse(EmployeeEntity employee) {
        DepartmentEntity department = employee.getDepartment();
        DepartmentResponse departmentResponse = new DepartmentResponse(department.getId(), department.getName(),
                department.getLocation(), department.getEmployees() == null ? 0 : department.getEmployees().size());
        Set<SkillResponse> skillResponses = employee.getSkills().stream().map(SkillResponse::skill)
                .collect(Collectors.toUnmodifiableSet());
        return new EmployeeResponse(employee.getId(), employee.getName(), employee.getEmail(),
                employee.getDesignation(), employee.getJoiningDate(), departmentResponse, skillResponses);
    }


}
