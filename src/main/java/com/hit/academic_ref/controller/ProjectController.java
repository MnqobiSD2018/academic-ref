package com.hit.academic_ref.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/years/{yearId}/projects")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<Project> getProjects(@PathVariable Long yearId) {
        return projectService.getProjectsByYear(yearId);
    }

    @PostMapping
    public Project createProject(@PathVariable Long yearId, @RequestBody Project project) {
        return projectService.createProject(yearId, project);
    }

    @PutMapping("/{projectId}/approve")
    public Project approveProject(@PathVariable Long projectId) {
        return projectService.updateStatus(projectId, Project.Status.APPROVED, null);
    }

    @PutMapping("/{projectId}/reject")
    public Project rejectProject(@PathVariable Long projectId,
                                @RequestBody Map<String, String> body) {
        return projectService.updateStatus(
            projectId, Project.Status.REJECTED, body.get("reason"));
    }

    // Search projects by title keyword across all years
    @GetMapping("/search")
    public List<Project> searchProjects(
            @RequestParam String keyword,
            @RequestParam(required = false) Project.Department department) {
        return projectService.searchProjects(keyword, department);
    }
}