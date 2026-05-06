
package com.hit.academic_ref.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAcademicYearId(Long yearId); // Spring generates this query automatically from the method name
    long countByAcademicYearId(Long yearId);

    // Filter by department within a year
    List<Project> findByAcademicYearIdAndDepartment(Long yearId, Project.Department department);

    // Filter by status within a year
    List<Project> findByAcademicYearIdAndStatus(Long academicYearId, Project.Status status);

    // Search by title keyword
    List<Project> findByTitleContainingIgnoreCase(String keyword);

    // Search by title AND department
    List<Project> findByTitleContainingIgnoreCaseAndDepartment(
        String keyword, Project.Department department);

    // Search by title AND level
    List<Project> findByTitleContainingIgnoreCaseAndLevel(
        String keyword, Project.Level level);

    // Search by title AND department AND level
    List<Project> findByTitleContainingIgnoreCaseAndDepartmentAndLevel(
        String keyword, Project.Department department, Project.Level level);

}