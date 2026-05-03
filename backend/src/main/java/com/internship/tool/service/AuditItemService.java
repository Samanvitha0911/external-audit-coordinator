package com.internship.tool.service;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.repository.AuditItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class AuditItemService {

    private final AuditItemRepository auditItemRepository;

    @Autowired
    public AuditItemService(AuditItemRepository auditItemRepository) {
        this.auditItemRepository = auditItemRepository;
    }

    public AuditItemDTO createAuditItem(CreateAuditItemRequest request, String createdBy) {
        AuditItem item = new AuditItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStatus(request.getStatus());
        item.setPriority(request.getPriority());
        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            LocalDate dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            item.setDueDate(dueDate);
        }
        item.setCreatedBy(createdBy);
        item.setAssignedTo(request.getAssignedTo());
        item.setDeleted(false);

        AuditItem saved = auditItemRepository.save(item);
        return mapToDTO(saved);
    }

    public Page<AuditItemDTO> getAllAuditItems(Pageable pageable) {
        return auditItemRepository.findAllActive(pageable)
                .map(this::mapToDTO);
    }

    public AuditItemDTO getAuditItemById(UUID id) {
        return auditItemRepository.findById(id)
                .filter(item -> !item.isDeleted())
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Audit item not found"));
    }

    public AuditItemDTO updateAuditItem(UUID id, CreateAuditItemRequest request, String updatedBy) {
        AuditItem item = auditItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Audit item not found"));

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStatus(request.getStatus());
        item.setPriority(request.getPriority());

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            LocalDate dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            item.setDueDate(dueDate);
        }

        item.setAssignedTo(request.getAssignedTo());
        item.setUpdatedAt(LocalDateTime.now());

        AuditItem saved = auditItemRepository.save(item);
        return mapToDTO(saved);
    }

    public void deleteAuditItem(UUID id) {
        auditItemRepository.softDeleteById(id);
    }

    public Page<AuditItemDTO> getByStatus(String status, Pageable pageable) {
        return auditItemRepository.findActiveByStatus(status, pageable)
                .map(this::mapToDTO);
    }

    private AuditItemDTO mapToDTO(AuditItem item) {
        AuditItemDTO dto = new AuditItemDTO();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setStatus(item.getStatus());
        dto.setPriority(item.getPriority());
        dto.setDueDate(item.getDueDate() != null ? item.getDueDate().atStartOfDay() : null);
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setCreatedBy(item.getCreatedBy());
        dto.setAssignedTo(item.getAssignedTo());
        return dto;
    }
}
