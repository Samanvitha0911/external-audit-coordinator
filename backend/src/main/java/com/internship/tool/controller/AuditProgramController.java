package com.internship.tool.controller;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import com.internship.tool.service.AuditProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-programs")
@RequiredArgsConstructor
public class AuditProgramController {

    private final AuditProgramService auditProgramService;

    @GetMapping("/all")
    public ResponseEntity<Page<AuditProgram>> getAllPrograms(Pageable pageable) {
        Page<AuditProgram> programs = auditProgramService.getAllPrograms(pageable);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditProgram> getProgramById(@PathVariable Long id) {
        AuditProgram program = auditProgramService.getProgramById(id);
        return ResponseEntity.ok(program);
    }

    @PostMapping("/create")
    public ResponseEntity<AuditProgram> createProgram(@Valid @RequestBody AuditProgram program, @RequestParam Long leadAuditorId) {
        AuditProgram createdProgram = auditProgramService.createProgram(program, leadAuditorId);
        return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditProgram> updateProgram(@PathVariable Long id, @Valid @RequestBody AuditProgram programDetails) {
        AuditProgram updatedProgram = auditProgramService.updateProgram(id, programDetails);
        return ResponseEntity.ok(updatedProgram);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AuditProgram> updateProgramStatus(@PathVariable Long id, @RequestParam AuditStatus status) {
        AuditProgram updatedProgram = auditProgramService.updateProgramStatus(id, status);
        return ResponseEntity.ok(updatedProgram);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        auditProgramService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
