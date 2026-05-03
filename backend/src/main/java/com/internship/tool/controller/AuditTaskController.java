package com.internship.tool.controller;

import com.internship.tool.entity.AuditStatus;
import com.internship.tool.entity.AuditTask;
import com.internship.tool.service.AuditTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-tasks")
@RequiredArgsConstructor
public class AuditTaskController {

    private final AuditTaskService auditTaskService;

    @GetMapping("/all")
    public ResponseEntity<Page<AuditTask>> getAllTasks(Pageable pageable) {
        Page<AuditTask> tasks = auditTaskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditTask> getTaskById(@PathVariable Long id) {
        AuditTask task = auditTaskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/create")
    public ResponseEntity<AuditTask> createTask(@Valid @RequestBody AuditTask task, @RequestParam Long programId, @RequestParam(required = false) Long assigneeId) {
        AuditTask createdTask = auditTaskService.createTask(programId, assigneeId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditTask> updateTask(@PathVariable Long id, @Valid @RequestBody AuditTask taskDetails) {
        AuditTask updatedTask = auditTaskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AuditTask> updateTaskStatus(@PathVariable Long id, @RequestParam AuditStatus status) {
        AuditTask updatedTask = auditTaskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<AuditTask> assignTask(@PathVariable Long id, @RequestParam Long assigneeId) {
        AuditTask updatedTask = auditTaskService.assignTask(id, assigneeId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        auditTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
