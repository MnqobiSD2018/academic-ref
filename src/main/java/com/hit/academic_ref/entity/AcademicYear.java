package com.hit.academic_ref.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data  // Lombok: auto-generates getters, setters, toString
@Table(name = "academic_years")
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String year; // e.g. "2023/2024"

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL)
    private List<Project> projects;
}