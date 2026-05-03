package com.hit.academic_ref.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hit.academic_ref.entity.AcademicYear;
import com.hit.academic_ref.repository.AcademicYearRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok: auto-generates constructor for final fields
public class AcademicYearService {

    private final AcademicYearRepository yearRepository;

    public List<AcademicYear> getAllYears() {
        return yearRepository.findAll();
    }

    public AcademicYear createYear(AcademicYear year) {
        return yearRepository.save(year);
    }

    public void deleteYear(Long id) {
        yearRepository.deleteById(id);
    }
}