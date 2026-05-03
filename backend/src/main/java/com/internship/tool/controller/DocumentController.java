package com.internship.tool.controller;

import com.internship.tool.entity.Document;
import com.internship.tool.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/all")
    public ResponseEntity<Page<Document>> getAllDocuments(Pageable pageable) {
        Page<Document> documents = documentService.getAllDocuments(pageable);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @Valid @RequestBody Document document,
            @RequestParam Long uploaderId,
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) Long taskId) {
        Document uploadedDocument = documentService.uploadDocument(document, uploaderId, programId, taskId);
        return new ResponseEntity<>(uploadedDocument, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocumentMetadata(@PathVariable Long id, @Valid @RequestBody Document documentDetails) {
        Document updatedDocument = documentService.updateDocumentMetadata(id, documentDetails);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
