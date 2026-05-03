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
}