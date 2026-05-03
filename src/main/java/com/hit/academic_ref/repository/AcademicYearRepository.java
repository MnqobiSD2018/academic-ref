
package com.hit.academic_ref.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    // JpaRepository gives you: findAll(), findById(), save(), deleteById() automatically
}