package com.internship.tool.repository;

import com.internship.tool.entity.Finding;
import com.internship.tool.entity.FindingSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FindingRepository extends JpaRepository<Finding, Long> {

    List<Finding> findByAuditProgramId(Long auditProgramId);

    List<Finding> findByRaisedById(Long userId);

    List<Finding> findByOwnerId(Long userId);

    List<Finding> findBySeverity(FindingSeverity severity);

    List<Finding> findByIsResolvedFalse();

    Optional<Finding> findByReferenceNumber(String referenceNumber);

    /** Open (unresolved) findings for a given program, ordered by severity. */
    @Query("""
            SELECT f FROM Finding f
            WHERE f.auditProgram.id = :programId
              AND f.isResolved = false
            ORDER BY
              CASE f.severity
                WHEN 'CRITICAL'      THEN 1
                WHEN 'HIGH'          THEN 2
                WHEN 'MEDIUM'        THEN 3
                WHEN 'LOW'           THEN 4
                WHEN 'INFORMATIONAL' THEN 5
              END
            """)
    List<Finding> findOpenFindingsByProgram(@Param("programId") Long programId);

    /** Count open findings grouped by severity — used for risk dashboard. */
    @Query("""
            SELECT f.severity, COUNT(f)
            FROM Finding f
            WHERE f.isResolved = false
            GROUP BY f.severity
            """)
    List<Object[]> countOpenFindingsBySeverity();
}
