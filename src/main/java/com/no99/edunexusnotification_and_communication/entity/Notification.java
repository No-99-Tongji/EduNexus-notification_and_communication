package com.no99.edunexusnotification_and_communication.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification Entity
 */
@Schema(description = "Notification entity representing a notification in the system")
public class Notification {

    @Schema(description = "Unique identifier for the notification", example = "1")
    private Long id;

    @Schema(description = "ID of the user who will receive this notification", example = "123")
    private Integer userId;

    @Schema(description = "Title of the notification", example = "New Assignment Posted")
    private String title;

    @Schema(description = "Content/message of the notification", example = "A new assignment has been posted for Mathematics course")
    private String content;

    @Schema(description = "Type/category of the notification")
    private NotificationType type;

    @Schema(description = "Priority level of the notification")
    private NotificationPriority priority;

    @Schema(description = "Whether the notification has been read by the user", example = "false")
    private Boolean isRead;

    @Schema(description = "Timestamp when the notification was read", example = "2024-11-10 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readAt;

    @Schema(description = "Timestamp when the notification was created", example = "2024-11-10 10:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the notification was last updated", example = "2024-11-10 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Expiration timestamp for the notification", example = "2024-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

    @Schema(description = "Additional metadata for the notification", example = "{\"courseId\": 456, \"assignmentId\": 789}")
    private Map<String, Object> metadata;

    @Schema(description = "Types of notifications available in the system")
    public enum NotificationType {
        SYSTEM, COURSE, ASSIGNMENT, ANNOUNCEMENT, MESSAGE
    }

    @Schema(description = "Priority levels for notifications")
    public enum NotificationPriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    // Constructors
    public Notification() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationPriority getPriority() { return priority; }
    public void setPriority(NotificationPriority priority) { this.priority = priority; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
