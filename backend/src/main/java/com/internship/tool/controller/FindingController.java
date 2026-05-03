package com.internship.tool.controller;

import com.internship.tool.entity.Finding;
import com.internship.tool.service.FindingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/findings")
@RequiredArgsConstructor
public class FindingController {

    private final FindingService findingService;

    @GetMapping("/all")
    public ResponseEntity<Page<Finding>> getAllFindings(Pageable pageable) {
        Page<Finding> findings = findingService.getAllFindings(pageable);
        return ResponseEntity.ok(findings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Finding> getFindingById(@PathVariable Long id) {
        Finding finding = findingService.getFindingById(id);
        return ResponseEntity.ok(finding);
    }

    @PostMapping("/create")
    public ResponseEntity<Finding> createFinding(
            @Valid @RequestBody Finding finding,
            @RequestParam Long programId,
            @RequestParam Long raisedById) {
        Finding createdFinding = findingService.createFinding(programId, raisedById, finding);
        return new ResponseEntity<>(createdFinding, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Finding> updateFinding(@PathVariable Long id, @Valid @RequestBody Finding findingDetails) {
        Finding updatedFinding = findingService.updateFinding(id, findingDetails);
        return ResponseEntity.ok(updatedFinding);
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Finding> resolveFinding(@PathVariable Long id) {
        Finding resolvedFinding = findingService.resolveFinding(id);
        return ResponseEntity.ok(resolvedFinding);
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Finding> assignOwner(@PathVariable Long id, @RequestParam Long ownerId) {
        Finding updatedFinding = findingService.assignOwner(id, ownerId);
        return ResponseEntity.ok(updatedFinding);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinding(@PathVariable Long id) {
        findingService.deleteFinding(id);
        return ResponseEntity.noContent().build();
    }
}
