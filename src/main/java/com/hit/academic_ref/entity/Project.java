// entity/Project.java
package com.hit.academic_ref.entity;

import java.util.List;

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
    @JoinColumn(name = "academic_year_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Document> documents;

    public enum Department {
        SE,
        IT,
        CS,
        ISAA
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    // Level of the project (HIT200 = year 2, HIT400 = year 4)
    public enum Level {
        HIT200, HIT400
    }

    @Enumerated(EnumType.STRING)
    private Level level;

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