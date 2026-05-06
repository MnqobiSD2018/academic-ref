package com.hit.academic_ref.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
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
    private final com.hit.academic_ref.service.DocumentService documentService;

    public List<Project> getProjectsByYear(Long yearId) {
        return projectRepository.findByAcademicYearId(yearId);
    }

    public Project createProject(Long yearId, Project project) {
        AcademicYear year = yearRepository.findById(yearId)
            .orElseThrow(() -> new RuntimeException("Year not found"));
        project.setAcademicYear(year);
        return projectRepository.save(project);
    }

    public void deleteProject(Long yearId, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getAcademicYear() == null || !project.getAcademicYear().getId().equals(yearId)) {
            throw new RuntimeException("Project does not belong to the given year");
        }

        projectRepository.delete(project);
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

    // Search projects by title keyword, optionally filter by department and/or level
    public List<Project> searchProjects(String keyword, Project.Department department, Project.Level level) {
        if (department != null && level != null) {
            return projectRepository.findByTitleContainingIgnoreCaseAndDepartmentAndLevel(keyword, department, level);
        }
        if (department != null) {
            return projectRepository.findByTitleContainingIgnoreCaseAndDepartment(keyword, department);
        }
        if (level != null) {
            return projectRepository.findByTitleContainingIgnoreCaseAndLevel(keyword, level);
        }
        return projectRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Admin: upload up to three documents for a project, set level/department, and mark as APPROVED
    public Project adminUploadDocuments(Long projectId, Project.Level level, Project.Department department,
            MultipartFile proposal, MultipartFile technical, MultipartFile finalDoc) throws IOException {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        if (level != null) project.setLevel(level);
        if (department != null) project.setDepartment(department);

        if (proposal != null && !proposal.isEmpty()) {
            documentService.uploadDocument(projectId, com.hit.academic_ref.entity.Document.DocumentType.PROPOSAL, proposal);
        }
        if (technical != null && !technical.isEmpty()) {
            documentService.uploadDocument(projectId, com.hit.academic_ref.entity.Document.DocumentType.TECHNICAL, technical);
        }
        if (finalDoc != null && !finalDoc.isEmpty()) {
            documentService.uploadDocument(projectId, com.hit.academic_ref.entity.Document.DocumentType.FINAL, finalDoc);
        }

        project.setStatus(Project.Status.APPROVED);
        return projectRepository.save(project);
    }
}