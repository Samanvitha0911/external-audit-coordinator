package com.internship.tool.service;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import com.internship.tool.entity.AuditTask;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.AuditTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditTaskService {

    private final AuditTaskRepository auditTaskRepository;
    private final AuditProgramService auditProgramService;
    private final UserService userService;

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "tasks", key = "#id")
    public AuditTask getTaskById(Long id) {
        return auditTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditTask", "id", id));
    }

    @Transactional(readOnly = true)
    public List<AuditTask> getTasksByProgramId(Long programId) {
        return auditTaskRepository.findByAuditProgramId(programId);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AuditTask> getAllTasks(org.springframework.data.domain.Pageable pageable) {
        return auditTaskRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<AuditTask> getTasksByAssigneeId(Long assigneeId) {
        return auditTaskRepository.findByAssignedToId(assigneeId);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask createTask(Long programId, Long assigneeId, AuditTask task) {
        AuditProgram program = auditProgramService.getProgramById(programId);
        task.setAuditProgram(program);

        if (assigneeId != null) {
            User assignee = userService.getUserById(assigneeId);
            task.setAssignedTo(assignee);
        }

        if (task.getStatus() == null) {
            task.setStatus(AuditStatus.PLANNED);
        }

        return auditTaskRepository.save(task);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask updateTask(Long id, AuditTask taskDetails) {
        AuditTask task = getTaskById(id);

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setPriority(taskDetails.getPriority());
        task.setNotes(taskDetails.getNotes());

        return auditTaskRepository.save(task);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask updateTaskStatus(Long id, AuditStatus newStatus) {
        AuditTask task = getTaskById(id);
        
        if (newStatus == AuditStatus.COMPLETED && task.getCompletedDate() == null) {
            task.setCompletedDate(LocalDate.now());
        }
        
        task.setStatus(newStatus);
        return auditTaskRepository.save(task);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask assignTask(Long taskId, Long assigneeId) {
        AuditTask task = getTaskById(taskId);
        User assignee = userService.getUserById(assigneeId);
        task.setAssignedTo(assignee);
        return auditTaskRepository.save(task);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public void deleteTask(Long id) {
        AuditTask task = getTaskById(id);
        auditTaskRepository.delete(task);
    }
}
