package com.internship.tool.repository;

import com.internship.tool.entity.AuditItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditItemRepository extends JpaRepository<AuditItem, UUID> {

    // Soft delete filter - active items only
    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false")
    Page<AuditItem> findAllActive(Pageable pageable);

    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false AND a.status = :status")
    Page<AuditItem> findActiveByStatus(@Param("status") String status, Pageable pageable);
   

    @Transactional
    @Modifying
    @Query("UPDATE AuditItem a SET a.isDeleted = true WHERE a.id = :id")
    void softDeleteById(@Param("id") UUID id);

}
