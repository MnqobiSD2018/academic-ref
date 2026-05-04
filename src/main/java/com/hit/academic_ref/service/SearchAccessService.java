package com.hit.academic_ref.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hit.academic_ref.entity.SearchAccess;
import com.hit.academic_ref.entity.User;
import com.hit.academic_ref.repository.SearchAccessRepository;
import com.hit.academic_ref.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchAccessService {
    private final SearchAccessRepository accessRepository;
    private final UserRepository userRepository;

    // Lecturer calls this
    public SearchAccess grantAccess(Long studentId, int validForDays) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        SearchAccess access = new SearchAccess();
        access.setStudent(student);
        access.setGrantedAt(LocalDateTime.now());
        access.setExpiresAt(LocalDateTime.now().plusDays(validForDays));
        access.setActive(true);

        return accessRepository.save(access);
    }

    // Check before allowing a student to search
    public boolean hasActiveAccess(Long studentId) {
        return accessRepository
            .findByStudentIdAndActiveTrue(studentId)
            .stream()
            .anyMatch(a -> LocalDateTime.now().isBefore(a.getExpiresAt()));
    }

    public void revokeAccess(Long accessId) {
        SearchAccess access = accessRepository.findById(accessId)
            .orElseThrow(() -> new RuntimeException("Access not found"));
        access.setActive(false);
        accessRepository.save(access);
    }
}
