package com.no99.edunexusnotification_and_communication.controller;

import com.no99.edunexusnotification_and_communication.dto.ApiResponse;
import com.no99.edunexusnotification_and_communication.dto.NotificationRequest;
import com.no99.edunexusnotification_and_communication.entity.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Notification REST Controller (Simplified Version)
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "APIs for managing notifications in EduNexus platform")
public class NotificationController {

    // In-memory storage for demo purposes
    private final Map<Long, Notification> notifications = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Create a new notification
     */
    @Operation(summary = "Create a new notification",
               description = "Creates a new notification for a specific user with given content and metadata")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification created successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Notification>> createNotification(
            @Parameter(description = "Notification request data", required = true)
            @Valid @RequestBody NotificationRequest request) {
        try {
            Notification notification = new Notification();
            notification.setId(idGenerator.getAndIncrement());
            notification.setUserId(request.getUserId());
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());
            notification.setType(request.getType());
            notification.setPriority(request.getPriority());
            notification.setExpiresAt(request.getExpiresAt());
            notification.setMetadata(request.getMetadata());
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());

            notifications.put(notification.getId(), notification);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(notification, "Notification created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create notification: " + e.getMessage()));
        }
    }

    /**
     * Get a notification by ID
     */
    @Operation(summary = "Get notification by ID",
               description = "Retrieves a specific notification by its unique identifier")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification found",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Notification>> getNotification(
            @Parameter(description = "Notification ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Notification notification = notifications.get(id);
            if (notification == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Notification not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(notification));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get notification: " + e.getMessage()));
        }
    }

    /**
     * Get notifications for a user
     */
    @Operation(summary = "Get notifications for a user",
               description = "Retrieves all notifications for a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User notifications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Notification>>> getUserNotifications(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable Integer userId) {
        try {
            List<Notification> userNotifications = new ArrayList<>();
            for (Notification notification : notifications.values()) {
                if (notification.getUserId().equals(userId)) {
                    userNotifications.add(notification);
                }
            }
            return ResponseEntity.ok(ApiResponse.success(userNotifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get notifications: " + e.getMessage()));
        }
    }

    /**
     * Mark notification as read
     */
    @Operation(summary = "Mark notification as read",
               description = "Marks a specific notification as read and updates the read timestamp")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @Parameter(description = "Notification ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Notification notification = notifications.get(id);
            if (notification == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Notification not found"));
            }

            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());

            return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to mark notification as read: " + e.getMessage()));
        }
    }

    /**
     * Delete a notification
     */
    @Operation(summary = "Delete a notification",
               description = "Permanently deletes a notification from the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @Parameter(description = "Notification ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Notification notification = notifications.remove(id);
            if (notification == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Notification not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(null, "Notification deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete notification: " + e.getMessage()));
        }
    }

    /**
     * Get all notifications (for testing)
     */
    @Operation(summary = "Get all notifications",
               description = "Retrieves all notifications in the system (for testing purposes)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All notifications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Request failed",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getAllNotifications() {
        try {
            List<Notification> allNotifications = new ArrayList<>(notifications.values());
            return ResponseEntity.ok(ApiResponse.success(allNotifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get notifications: " + e.getMessage()));
        }
    }
}
