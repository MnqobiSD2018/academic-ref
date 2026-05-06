package com.hit.academic_ref.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hit.academic_ref.entity.Document;
import com.hit.academic_ref.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // GET all documents for a project
    @GetMapping("/api/projects/{projectId}/documents")
    public List<Document> getDocuments(@PathVariable Long projectId) {
        return documentService.getDocumentsByProject(projectId);
    }

    // GET download/view a document
    @GetMapping("/api/documents/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws MalformedURLException {
        Path filePath = documentService.getFilePath(documentId);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }
}