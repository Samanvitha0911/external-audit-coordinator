package com.internship.tool.service;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditTask;
import com.internship.tool.entity.Document;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuditProgramService auditProgramService;
    private final AuditTaskService auditTaskService;
    private final UserService userService;

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "documents", key = "#id")
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsByProgramId(Long programId) {
        return documentRepository.findByAuditProgramId(programId);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Document> getAllDocuments(org.springframework.data.domain.Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsByTaskId(Long taskId) {
        return documentRepository.findByAuditTaskId(taskId);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public Document uploadDocument(Document document, Long uploaderId, Long programId, Long taskId) {
        if (programId == null && taskId == null) {
            throw new ValidationException("Document must be associated with either an AuditProgram or an AuditTask.");
        }

        User uploader = userService.getUserById(uploaderId);
        document.setUploadedBy(uploader);

        if (programId != null) {
            AuditProgram program = auditProgramService.getProgramById(programId);
            document.setAuditProgram(program);
        }

        if (taskId != null) {
            AuditTask task = auditTaskService.getTaskById(taskId);
            document.setAuditTask(task);
            // If task is provided, optionally link to the program as well
            if (document.getAuditProgram() == null && task.getAuditProgram() != null) {
                document.setAuditProgram(task.getAuditProgram());
            }
        }

        return documentRepository.save(document);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public Document updateDocumentMetadata(Long id, Document documentDetails) {
        Document document = getDocumentById(id);
        
        document.setDescription(documentDetails.getDescription());
        document.setDocumentType(documentDetails.getDocumentType());
        document.setIsConfidential(documentDetails.getIsConfidential());

        return documentRepository.save(document);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public void deleteDocument(Long id) {
        Document document = getDocumentById(id);
        // Note: In a real-world app, you'd also need to delete the physical file from the storage (e.g. S3 or local FS)
        documentRepository.delete(document);
    }
}
