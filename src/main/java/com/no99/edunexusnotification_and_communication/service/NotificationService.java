package com.no99.edunexusnotification_and_communication.service;

import com.no99.edunexusnotification_and_communication.entity.Notification;

import java.util.List;

/**
 * Notification Service Interface
 */
public interface NotificationService {

    /**
     * Create a new notification
     */
    Notification createNotification(Notification notification);

    /**
     * Update an existing notification
     */
    Notification updateNotification(Notification notification);

    /**
     * Delete a notification by ID
     */
    boolean deleteNotification(Long id);

    /**
     * Get a notification by ID
     */
    Notification getNotificationById(Long id);

    /**
     * Get all notifications for a specific user
     */
    List<Notification> getNotificationsByUserId(Integer userId);

    /**
     * Get unread notifications for a specific user
     */
    List<Notification> getUnreadNotificationsByUserId(Integer userId);

    /**
     * Get all notifications
     */
    List<Notification> getAllNotifications();

    /**
     * Mark a notification as read
     */
    boolean markNotificationAsRead(Long id);

    /**
     * Mark all notifications as read for a user
     */
    boolean markAllNotificationsAsRead(Integer userId);

    /**
     * Count unread notifications for a user
     */
    int countUnreadNotifications(Integer userId);

    /**
     * Delete expired notifications
     */
    int deleteExpiredNotifications();

    /**
     * Get notifications by user ID and type
     */
    List<Notification> getNotificationsByUserIdAndType(Integer userId, String type);

    /**
     * Get notifications by user ID and priority
     */
    List<Notification> getNotificationsByUserIdAndPriority(Integer userId, String priority);
}

