package com.no99.edunexusnotification_and_communication.dto;

import com.no99.edunexusnotification_and_communication.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification creation/update request DTO
 */
@Schema(description = "Notification creation request")
public class NotificationRequest {

    @Schema(description = "User ID who will receive the notification", example = "123", required = true)
    @NotNull(message = "User ID is required")
    private Integer userId;

    @Schema(description = "Notification title", example = "New Assignment Posted", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Notification content/message", example = "A new assignment has been posted for Mathematics course", required = true)
    @NotBlank(message = "Content is required")
    private String content;

    @Schema(description = "Type of notification", example = "ASSIGNMENT", defaultValue = "SYSTEM")
    private Notification.NotificationType type = Notification.NotificationType.SYSTEM;

    @Schema(description = "Priority level of notification", example = "HIGH", defaultValue = "MEDIUM")
    private Notification.NotificationPriority priority = Notification.NotificationPriority.MEDIUM;

    @Schema(description = "Expiration date and time for the notification", example = "2024-12-31T23:59:59")
    private LocalDateTime expiresAt;

    @Schema(description = "Additional metadata for the notification", example = "{\"courseId\": 456, \"assignmentId\": 789}")
    private Map<String, Object> metadata;

    // Constructors
    public NotificationRequest() {}

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Notification.NotificationType getType() { return type; }
    public void setType(Notification.NotificationType type) { this.type = type; }

    public Notification.NotificationPriority getPriority() { return priority; }
    public void setPriority(Notification.NotificationPriority priority) { this.priority = priority; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
