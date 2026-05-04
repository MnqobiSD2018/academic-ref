package com.hit.academic_ref.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.SearchAccess;

public interface SearchAccessRepository extends JpaRepository<SearchAccess, Long> {
    List<SearchAccess> findByStudentIdAndActiveTrue(Long studentId);
}
