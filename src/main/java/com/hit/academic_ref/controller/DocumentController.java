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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hit.academic_ref.entity.Document;
import com.hit.academic_ref.entity.UploadToken;
import com.hit.academic_ref.security.UploadTokenService;
import com.hit.academic_ref.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final UploadTokenService uploadTokenService;

    // GET all documents for a project
    @GetMapping("/api/projects/{projectId}/documents")
    public List<Document> getDocuments(@PathVariable Long projectId) {
        return documentService.getDocumentsByProject(projectId);
    }

    // POST upload a document
    @PostMapping("/api/upload/{tokenString}")
        public ResponseEntity<?> uploadViaToken(
            @PathVariable String tokenString,
            @RequestParam("type") Document.DocumentType type,
            @RequestParam("file") MultipartFile file) {
    try {
        // 1. Validate the token first
        UploadToken token = uploadTokenService.validateToken(tokenString);

        // 2. Upload the document to the project linked to this token
        documentService.uploadDocument(token.getProject().getId(), type, file);

        // 3. Check if all 3 document types have been uploaded
        List<Document> docs = documentService.getDocumentsByProject(
            token.getProject().getId());

        boolean hasAll3 = docs.stream().map(Document::getType).distinct().count() == 3;
        if (hasAll3) {
            token.setUsed(true); // mark link as fully used
            uploadTokenService.save(token);
        }

        return ResponseEntity.ok("Document uploaded successfully");
    } catch (RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    } catch (IOException e) {
        return ResponseEntity.status(500).body("File upload failed");
    }
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