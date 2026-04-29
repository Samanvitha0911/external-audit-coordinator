package com.internship.tool.repository;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuditProgramRepository extends JpaRepository<AuditProgram, Long> {

    List<AuditProgram> findByStatus(AuditStatus status);

    List<AuditProgram> findByLeadAuditorId(Long leadAuditorId);

    List<AuditProgram> findByIsExternalTrue();

    List<AuditProgram> findByDepartmentUnderAuditIgnoreCase(String department);

    /** Programs whose planned end date is before today and not yet completed. */
    @Query("""
            SELECT ap FROM AuditProgram ap
            WHERE ap.plannedEndDate < :today
              AND ap.status NOT IN ('COMPLETED', 'CANCELLED')
            """)
    List<AuditProgram> findOverduePrograms(@Param("today") LocalDate today);

    /** Count programs grouped by status — useful for dashboard metrics. */
    @Query("SELECT ap.status, COUNT(ap) FROM AuditProgram ap GROUP BY ap.status")
    List<Object[]> countByStatus();
}
