package com.hit.academic_ref.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hit.academic_ref.entity.AcademicYear;
import com.hit.academic_ref.service.AcademicYearService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/years")
@CrossOrigin(origins = "http://localhost:5173") // Allow React dev server
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearService yearService;

    @GetMapping
    public List<AcademicYear> getAllYears() {
        return yearService.getAllYears();
    }

    @PostMapping
    public AcademicYear createYear(@RequestBody java.util.Map<String, String> body) {
        AcademicYear year = new AcademicYear();
        year.setYear(body.get("year"));
        return yearService.createYear(year);
    }

    @GetMapping("/{id}/delete-preview")
    public Map<String, Object> getDeletePreview(@PathVariable Long id) {
        return yearService.getDeletePreview(id);
    }

    @DeleteMapping("/{id}")
    public void deleteYear(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        String confirmationText = body == null ? null : (String) body.get("confirmationText");
        boolean deleteProjects = body != null && Boolean.TRUE.equals(body.get("deleteProjects"));
        yearService.deleteYearSafely(id, confirmationText, deleteProjects);
    }
}