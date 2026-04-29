package com.internship.tool.repository;

import com.internship.tool.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByAuditProgramId(Long auditProgramId);

    List<Document> findByAuditTaskId(Long auditTaskId);

    List<Document> findByUploadedById(Long userId);

    List<Document> findByDocumentTypeIgnoreCase(String documentType);

    List<Document> findByAuditProgramIdAndIsConfidentialFalse(Long auditProgramId);
}
