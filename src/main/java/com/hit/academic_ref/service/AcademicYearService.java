package com.hit.academic_ref.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.hit.academic_ref.entity.AcademicYear;
import com.hit.academic_ref.entity.Project;
import com.hit.academic_ref.repository.AcademicYearRepository;
import com.hit.academic_ref.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok: auto-generates constructor for final fields
public class AcademicYearService {

    private final AcademicYearRepository yearRepository;
    private final ProjectRepository projectRepository;

    public List<AcademicYear> getAllYears() {
        return yearRepository.findAll();
    }

    public AcademicYear createYear(AcademicYear year) {
        return yearRepository.save(year);
    }

    public Map<String, Object> getDeletePreview(Long id) {
        AcademicYear year = yearRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Year not found"));
        long projectCount = projectRepository.countByAcademicYearId(id);

        return Map.of(
            "yearId", year.getId(),
            "year", year.getYear(),
            "projectCount", projectCount,
            "requiredConfirmationText", "DELETE YEAR " + year.getYear()
        );
    }

    public void deleteYearSafely(Long id, String confirmationText, boolean deleteProjects) {
        AcademicYear year = yearRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Year not found"));

        String requiredText = "DELETE YEAR " + year.getYear();
        if (confirmationText == null || !requiredText.equals(confirmationText.trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid confirmation text");
        }

        List<Project> projects = projectRepository.findByAcademicYearId(id);
        if (!projects.isEmpty() && !deleteProjects) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Year has associated projects. Set deleteProjects=true to continue."
            );
        }

        yearRepository.delete(year);
    }
}