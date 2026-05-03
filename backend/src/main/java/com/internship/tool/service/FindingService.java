package com.internship.tool.service;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.Finding;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.FindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindingService {

    private final FindingRepository findingRepository;
    private final AuditProgramService auditProgramService;
    private final UserService userService;

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "findings", key = "#id")
    public Finding getFindingById(Long id) {
        return findingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finding", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Finding> getFindingsByProgramId(Long programId) {
        return findingRepository.findByAuditProgramId(programId);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Finding> getAllFindings(org.springframework.data.domain.Pageable pageable) {
        return findingRepository.findAll(pageable);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"findings", "findingsList", "findingsPaginated"}, allEntries = true)
    public Finding createFinding(Long programId, Long raisedById, Finding finding) {
        AuditProgram program = auditProgramService.getProgramById(programId);
        User raisedBy = userService.getUserById(raisedById);

        finding.setAuditProgram(program);
        finding.setRaisedBy(raisedBy);
        
        if (finding.getReferenceNumber() == null || finding.getReferenceNumber().isBlank()) {
            finding.setReferenceNumber("FIND-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        return findingRepository.save(finding);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"findings", "findingsList", "findingsPaginated"}, allEntries = true)
    public Finding updateFinding(Long id, Finding findingDetails) {
        Finding finding = getFindingById(id);

        finding.setTitle(findingDetails.getTitle());
        finding.setDescription(findingDetails.getDescription());
        finding.setSeverity(findingDetails.getSeverity());
        finding.setRecommendation(findingDetails.getRecommendation());
        finding.setManagementResponse(findingDetails.getManagementResponse());
        finding.setActionPlan(findingDetails.getActionPlan());
        finding.setTargetRemediationDate(findingDetails.getTargetRemediationDate());
        
        return findingRepository.save(finding);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"findings", "findingsList", "findingsPaginated"}, allEntries = true)
    public Finding resolveFinding(Long id) {
        Finding finding = getFindingById(id);
        finding.setIsResolved(true);
        finding.setActualRemediationDate(LocalDate.now());
        return findingRepository.save(finding);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"findings", "findingsList", "findingsPaginated"}, allEntries = true)
    public Finding assignOwner(Long findingId, Long ownerId) {
        Finding finding = getFindingById(findingId);
        User owner = userService.getUserById(ownerId);
        finding.setOwner(owner);
        return findingRepository.save(finding);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"findings", "findingsList", "findingsPaginated"}, allEntries = true)
    public void deleteFinding(Long id) {
        Finding finding = getFindingById(id);
        findingRepository.delete(finding);
    }
}
