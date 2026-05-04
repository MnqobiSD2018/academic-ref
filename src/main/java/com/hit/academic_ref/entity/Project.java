// entity/Project.java
package com.hit.academic_ref.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    // Many projects belong to one academic year
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Document> documents;

    public enum Department {
    SOFTWARE_ENGINEERING,
    INFORMATION_TECHNOLOGY,
    COMPUTER_SCIENCE,
    INFORMATION_SECURITY_AND_ASSURANCE
}
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    // In Project.java add:
    public enum Status {
        PENDING,   // just created, awaiting documents
        UPLOADED,  // student uploaded docs, awaiting lecturer review
        APPROVED,  // lecturer approved, visible to all
        REJECTED   // sent back for corrections
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private String rejectionReason; // lecturer fills this if rejected
}