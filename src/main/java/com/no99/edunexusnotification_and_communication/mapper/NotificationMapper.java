package com.no99.edunexusnotification_and_communication.mapper;

import com.no99.edunexusnotification_and_communication.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis Mapper for Notification entity
 */
@Mapper
public interface NotificationMapper {

    /**
     * Insert a new notification
     */
    int insert(Notification notification);

    /**
     * Update a notification
     */
    int update(Notification notification);

    /**
     * Delete a notification by ID
     */
    int deleteById(Long id);

    /**
     * Find a notification by ID
     */
    Notification findById(Long id);

    /**
     * Find all notifications for a specific user
     */
    List<Notification> findByUserId(Integer userId);

    /**
     * Find unread notifications for a specific user
     */
    List<Notification> findUnreadByUserId(Integer userId);

    /**
     * Find all notifications
     */
    List<Notification> findAll();

    /**
     * Mark notification as read
     */
    int markAsRead(Long id);

    /**
     * Mark all notifications as read for a user
     */
    int markAllAsReadByUserId(Integer userId);

    /**
     * Count unread notifications for a user
     */
    int countUnreadByUserId(Integer userId);

    /**
     * Delete expired notifications
     */
    int deleteExpired();

    /**
     * Find notifications by type and user
     */
    List<Notification> findByUserIdAndType(@Param("userId") Integer userId,
                                            @Param("type") String type);

    /**
     * Find notifications by priority and user
     */
    List<Notification> findByUserIdAndPriority(@Param("userId") Integer userId,
                                                 @Param("priority") String priority);
}

