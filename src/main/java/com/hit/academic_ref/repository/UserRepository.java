package com.hit.academic_ref.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hit.academic_ref.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
