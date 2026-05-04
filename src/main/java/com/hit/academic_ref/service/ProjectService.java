package com.hit.academic_ref.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hit.academic_ref.entity.AcademicYear;
import com.hit.academic_ref.entity.Project;
import com.hit.academic_ref.repository.AcademicYearRepository;
import com.hit.academic_ref.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AcademicYearRepository yearRepository;

    public List<Project> getProjectsByYear(Long yearId) {
        return projectRepository.findByAcademicYearId(yearId);
    }

    public Project createProject(Long yearId, Project project) {
        AcademicYear year = yearRepository.findById(yearId)
            .orElseThrow(() -> new RuntimeException("Year not found"));
        project.setAcademicYear(year);
        return projectRepository.save(project);
    }

    public Project updateStatus(Long projectId, Project.Status status, String reason) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RuntimeException("Project not found"));
    project.setStatus(status);
    project.setRejectionReason(reason);
    return projectRepository.save(project);
}

    // Only return approved projects to general browsing
    public List<Project> getApprovedProjectsByYear(Long yearId) {
        return projectRepository.findByAcademicYearIdAndStatus(
            yearId, Project.Status.APPROVED);
    }
}