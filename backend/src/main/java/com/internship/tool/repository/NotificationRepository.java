package com.internship.tool.repository;

import com.internship.tool.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /** All notifications for a recipient, newest first. */
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    /** Only unread notifications for a recipient. */
    List<Notification> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long recipientId);

    /** Count of unread notifications — used for badge display. */
    long countByRecipientIdAndIsReadFalse(Long recipientId);

    List<Notification> findByNotificationTypeAndRecipientId(
            String notificationType, Long recipientId);

    /** Bulk-mark all unread notifications as read for a given user. */
    @Modifying
    @Transactional
    @Query("""
            UPDATE Notification n
               SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP
             WHERE n.recipient.id = :recipientId
               AND n.isRead = false
            """)
    int markAllAsRead(@Param("recipientId") Long recipientId);
}
