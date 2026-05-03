package com.hit.academic_ref.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByProjectId(Long projectId);
}