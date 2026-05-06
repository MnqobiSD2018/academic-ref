package com.hit.academic_ref.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hit.academic_ref.entity.Project;
import com.hit.academic_ref.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Search projects by title keyword across all years
    @GetMapping("/api/projects/search")
    public List<Project> searchProjects(
            @RequestParam String keyword,
            @RequestParam(required = false) Project.Department department,
            @RequestParam(required = false) Project.Level level) {
        return projectService.searchProjects(keyword, department, level);
    }

    // Admin: one-time upload of up to 3 documents for a project and set level/department
    @PostMapping("/api/admin/projects/{projectId}/upload")
    public ResponseEntity<?> adminUpload(
            @PathVariable Long projectId,
            @RequestParam(required = false) Project.Level level,
            @RequestParam(required = false) Project.Department department,
            @RequestParam(value = "proposal", required = false) MultipartFile proposal,
            @RequestParam(value = "technical", required = false) MultipartFile technical,
            @RequestParam(value = "final", required = false) MultipartFile finalDoc) {
        // simple PDF validation helper
        Predicate<MultipartFile> isPdf = f -> f == null || f.isEmpty() || (
            f.getContentType() != null && f.getContentType().equalsIgnoreCase("application/pdf")
            ) || (f.getOriginalFilename() != null && f.getOriginalFilename().toLowerCase().endsWith(".pdf"));

        if (!isPdf.test(proposal) || !isPdf.test(technical) || !isPdf.test(finalDoc)) {
            return ResponseEntity.badRequest().body("All uploaded files must be PDFs");
        }

        try {
            Project updated = projectService.adminUploadDocuments(projectId, level, department, proposal, technical, finalDoc);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/api/years/{yearId}/projects")
    public List<Project> getProjects(@PathVariable Long yearId) {
        return projectService.getProjectsByYear(yearId);
    }

    @PostMapping("/api/years/{yearId}/projects")
    public Project createProject(@PathVariable Long yearId, @RequestBody Project project) {
        return projectService.createProject(yearId, project);
    }

    @DeleteMapping("/api/years/{yearId}/projects/{projectId}")
    public void deleteProject(@PathVariable Long yearId, @PathVariable Long projectId) {
        projectService.deleteProject(yearId, projectId);
    }

    @PutMapping("/api/years/{yearId}/projects/{projectId}/approve")
    public Project approveProject(@PathVariable Long projectId) {
        return projectService.updateStatus(projectId, Project.Status.APPROVED, null);
    }

    @PutMapping("/api/years/{yearId}/projects/{projectId}/reject")
    public Project rejectProject(@PathVariable Long projectId,
                                @RequestBody Map<String, String> body) {
        return projectService.updateStatus(
            projectId, Project.Status.REJECTED, body.get("reason"));
    }
}