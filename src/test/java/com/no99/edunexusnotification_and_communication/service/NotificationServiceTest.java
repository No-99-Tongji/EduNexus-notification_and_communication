package com.no99.edunexusnotification_and_communication.service;

import com.no99.edunexusnotification_and_communication.entity.Notification;
import com.no99.edunexusnotification_and_communication.entity.Notification.NotificationPriority;
import com.no99.edunexusnotification_and_communication.entity.Notification.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for NotificationService with database
 */
@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void testCreateNotification() {
        // Create a test notification
        Notification notification = new Notification();
        notification.setUserId(999);
        notification.setTitle("Test Notification");
        notification.setContent("This is a test notification content");
        notification.setType(NotificationType.SYSTEM);
        notification.setPriority(NotificationPriority.HIGH);
        notification.setExpiresAt(LocalDateTime.now().plusDays(7));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testKey", "testValue");
        metadata.put("courseId", 123);
        notification.setMetadata(metadata);

        // Save to database
        Notification created = notificationService.createNotification(notification);

        // Verify
        assertNotNull(created.getId());
        assertEquals(999, created.getUserId());
        assertEquals("Test Notification", created.getTitle());
        assertEquals(NotificationType.SYSTEM, created.getType());
        assertEquals(NotificationPriority.HIGH, created.getPriority());
        assertFalse(created.getIsRead());
        assertNotNull(created.getCreatedAt());
    }

    @Test
    public void testGetNotificationsByUserId() {
        // Create test notifications
        for (int i = 0; i < 3; i++) {
            Notification notification = new Notification();
            notification.setUserId(888);
            notification.setTitle("Test Notification " + i);
            notification.setContent("Content " + i);
            notification.setType(NotificationType.COURSE);
            notification.setPriority(NotificationPriority.MEDIUM);
            notificationService.createNotification(notification);
        }

        // Retrieve notifications
        List<Notification> notifications = notificationService.getNotificationsByUserId(888);

        // Verify
        assertTrue(notifications.size() >= 3);
    }

    @Test
    public void testMarkNotificationAsRead() {
        // Create a notification
        Notification notification = new Notification();
        notification.setUserId(777);
        notification.setTitle("Mark as Read Test");
        notification.setContent("This notification will be marked as read");
        notification.setType(NotificationType.ANNOUNCEMENT);
        notification.setPriority(NotificationPriority.LOW);

        Notification created = notificationService.createNotification(notification);
        assertFalse(created.getIsRead());

        // Mark as read
        boolean success = notificationService.markNotificationAsRead(created.getId());
        assertTrue(success);

        // Verify it's marked as read
        Notification updated = notificationService.getNotificationById(created.getId());
        assertTrue(updated.getIsRead());
        assertNotNull(updated.getReadAt());
    }

    @Test
    public void testCountUnreadNotifications() {
        // Create unread notifications
        for (int i = 0; i < 5; i++) {
            Notification notification = new Notification();
            notification.setUserId(666);
            notification.setTitle("Unread Test " + i);
            notification.setContent("Content " + i);
            notification.setType(NotificationType.MESSAGE);
            notification.setPriority(NotificationPriority.URGENT);
            notificationService.createNotification(notification);
        }

        // Count unread
        int count = notificationService.countUnreadNotifications(666);
        assertTrue(count >= 5);
    }

    @Test
    public void testDeleteNotification() {
        // Create a notification
        Notification notification = new Notification();
        notification.setUserId(555);
        notification.setTitle("Delete Test");
        notification.setContent("This notification will be deleted");
        notification.setType(NotificationType.SYSTEM);
        notification.setPriority(NotificationPriority.MEDIUM);

        Notification created = notificationService.createNotification(notification);
        Long id = created.getId();
        assertNotNull(id);

        // Delete it
        boolean success = notificationService.deleteNotification(id);
        assertTrue(success);

        // Verify it's deleted
        Notification deleted = notificationService.getNotificationById(id);
        assertNull(deleted);
    }
}

