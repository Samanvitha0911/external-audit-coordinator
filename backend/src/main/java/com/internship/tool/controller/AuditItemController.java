package com.internship.tool.controller;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.service.AuditItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/audit-items")
public class AuditItemController {

    private final AuditItemService auditItemService;

    public AuditItemController(AuditItemService auditItemService) {
        this.auditItemService = auditItemService;
    }

    @PostMapping
    public ResponseEntity<AuditItemDTO> createAuditItem(@Valid @RequestBody CreateAuditItemRequest request, @RequestHeader("X-User-Id") String userId) {
        AuditItemDTO created = auditItemService.createAuditItem(request, userId);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<Page<AuditItemDTO>> getAllAuditItems(Pageable pageable) {
        Page<AuditItemDTO> items = auditItemService.getAllAuditItems(pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditItemDTO> getAuditItem(@PathVariable UUID id) {
        AuditItemDTO item = auditItemService.getAuditItemById(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditItemDTO> updateAuditItem(@PathVariable UUID id, @Valid @RequestBody CreateAuditItemRequest request, @RequestHeader("X-User-Id") String userId){
        AuditItemDTO updated = auditItemService.updateAuditItem(id, request, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditItem(@PathVariable UUID id) {
        auditItemService.deleteAuditItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AuditItemDTO>> getByStatus(@PathVariable String status, Pageable pageable) {
        Page<AuditItemDTO> items = auditItemService.getByStatus(status, pageable);
        return ResponseEntity.ok(items);
    }
}
