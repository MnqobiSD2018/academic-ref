package com.hit.academic_ref.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hit.academic_ref.entity.Document;
import com.hit.academic_ref.entity.Project;
import com.hit.academic_ref.repository.DocumentRepository;
import com.hit.academic_ref.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    @Value("${app.upload.dir}")  // reads from application.properties
    private String uploadDir;

    public List<Document> getDocumentsByProject(Long projectId) {
        return documentRepository.findByProjectId(projectId);
    }

    public Document uploadDocument(Long projectId, Document.DocumentType type, MultipartFile file) throws IOException {
        // 1. Find the project
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        // 2. Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Save file with a unique name to avoid conflicts
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 4. Save document record to database
        Document doc = new Document();
        doc.setProject(project);
        doc.setType(type);
        doc.setFileName(file.getOriginalFilename());
        doc.setFilePath(filePath.toString());

        return documentRepository.save(doc);
    }

    public Path getFilePath(Long documentId) {
        Document doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        return Paths.get(doc.getFilePath());
    }
}