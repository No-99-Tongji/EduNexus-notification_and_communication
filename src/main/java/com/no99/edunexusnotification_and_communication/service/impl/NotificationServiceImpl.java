package com.no99.edunexusnotification_and_communication.service.impl;

import com.no99.edunexusnotification_and_communication.entity.Notification;
import com.no99.edunexusnotification_and_communication.mapper.NotificationMapper;
import com.no99.edunexusnotification_and_communication.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Notification Service Implementation
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(Notification notification) {
        logger.info("Creating notification for user: {}", notification.getUserId());

        // Set default values if not provided
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }
        if (notification.getType() == null) {
            notification.setType(Notification.NotificationType.SYSTEM);
        }
        if (notification.getPriority() == null) {
            notification.setPriority(Notification.NotificationPriority.MEDIUM);
        }

        int result = notificationMapper.insert(notification);
        if (result > 0) {
            logger.info("Notification created successfully with ID: {}", notification.getId());
            return notification;
        } else {
            logger.error("Failed to create notification");
            throw new RuntimeException("Failed to create notification");
        }
    }

    @Override
    public Notification updateNotification(Notification notification) {
        logger.info("Updating notification with ID: {}", notification.getId());

        Notification existingNotification = notificationMapper.findById(notification.getId());
        if (existingNotification == null) {
            logger.error("Notification not found with ID: {}", notification.getId());
            throw new RuntimeException("Notification not found");
        }

        int result = notificationMapper.update(notification);
        if (result > 0) {
            logger.info("Notification updated successfully");
            return notificationMapper.findById(notification.getId());
        } else {
            logger.error("Failed to update notification");
            throw new RuntimeException("Failed to update notification");
        }
    }

    @Override
    public boolean deleteNotification(Long id) {
        logger.info("Deleting notification with ID: {}", id);

        int result = notificationMapper.deleteById(id);
        if (result > 0) {
            logger.info("Notification deleted successfully");
            return true;
        } else {
            logger.warn("Notification not found or already deleted with ID: {}", id);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Notification getNotificationById(Long id) {
        logger.info("Getting notification with ID: {}", id);
        return notificationMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserId(Integer userId) {
        logger.info("Getting notifications for user: {}", userId);
        return notificationMapper.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotificationsByUserId(Integer userId) {
        logger.info("Getting unread notifications for user: {}", userId);
        return notificationMapper.findUnreadByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        logger.info("Getting all notifications");
        return notificationMapper.findAll();
    }

    @Override
    public boolean markNotificationAsRead(Long id) {
        logger.info("Marking notification as read with ID: {}", id);

        int result = notificationMapper.markAsRead(id);
        if (result > 0) {
            logger.info("Notification marked as read successfully");
            return true;
        } else {
            logger.warn("Notification not found with ID: {}", id);
            return false;
        }
    }

    @Override
    public boolean markAllNotificationsAsRead(Integer userId) {
        logger.info("Marking all notifications as read for user: {}", userId);

        int result = notificationMapper.markAllAsReadByUserId(userId);
        logger.info("Marked {} notifications as read for user: {}", result, userId);
        return result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public int countUnreadNotifications(Integer userId) {
        logger.info("Counting unread notifications for user: {}", userId);
        return notificationMapper.countUnreadByUserId(userId);
    }

    @Override
    public int deleteExpiredNotifications() {
        logger.info("Deleting expired notifications");

        int result = notificationMapper.deleteExpired();
        logger.info("Deleted {} expired notifications", result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserIdAndType(Integer userId, String type) {
        logger.info("Getting notifications for user: {} with type: {}", userId, type);
        return notificationMapper.findByUserIdAndType(userId, type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserIdAndPriority(Integer userId, String priority) {
        logger.info("Getting notifications for user: {} with priority: {}", userId, priority);
        return notificationMapper.findByUserIdAndPriority(userId, priority);
    }
}

