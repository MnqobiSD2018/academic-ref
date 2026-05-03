
package com.hit.academic_ref.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAcademicYearId(Long yearId); // Spring generates this query automatically from the method name
}