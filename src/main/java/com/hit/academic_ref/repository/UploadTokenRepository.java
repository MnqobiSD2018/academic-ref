package com.hit.academic_ref.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.UploadToken;

public interface UploadTokenRepository extends JpaRepository<UploadToken, Long> {
    Optional<UploadToken> findByToken(String token);
}
