package com.internship.tool.service;

import com.internship.tool.entity.Notification;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "notifications", key = "#id")
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<Notification> getAllNotifications(org.springframework.data.domain.Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"notifications", "notificationsList", "notificationsPaginated", "unreadCount"}, allEntries = true)
    public Notification createNotification(Long recipientId, Long senderId, Notification notification) {
        User recipient = userService.getUserById(recipientId);
        notification.setRecipient(recipient);

        if (senderId != null) {
            User sender = userService.getUserById(senderId);
            notification.setSender(sender);
        }

        return notificationRepository.save(notification);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"notifications", "notificationsList", "notificationsPaginated", "unreadCount"}, allEntries = true)
    public Notification markAsRead(Long id) {
        Notification notification = getNotificationById(id);
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"notifications", "notificationsList", "notificationsPaginated", "unreadCount"}, allEntries = true)
    public int markAllAsReadForUser(Long userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"notifications", "notificationsList", "notificationsPaginated", "unreadCount"}, allEntries = true)
    public void deleteNotification(Long id) {
        Notification notification = getNotificationById(id);
        notificationRepository.delete(notification);
    }
}
