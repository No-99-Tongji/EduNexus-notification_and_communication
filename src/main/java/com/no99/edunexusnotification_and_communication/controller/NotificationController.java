package com.no99.edunexusnotification_and_communication.controller;

import com.no99.edunexusnotification_and_communication.dto.ApiResponse;
import com.no99.edunexusnotification_and_communication.dto.NotificationRequest;
import com.no99.edunexusnotification_and_communication.entity.Notification;
import com.no99.edunexusnotification_and_communication.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Notification REST Controller
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "APIs for managing notifications in EduNexus platform")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

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
            notification.setUserId(request.getUserId());
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());
            notification.setType(request.getType());
            notification.setPriority(request.getPriority());
            notification.setExpiresAt(request.getExpiresAt());
            notification.setMetadata(request.getMetadata());

            Notification created = notificationService.createNotification(notification);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Notification created successfully"));
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
            Notification notification = notificationService.getNotificationById(id);
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
            List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId);
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
            boolean success = notificationService.markNotificationAsRead(id);
            if (!success) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Notification not found"));
            }


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
            boolean success = notificationService.deleteNotification(id);
            if (!success) {
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
            List<Notification> allNotifications = notificationService.getAllNotifications();
            return ResponseEntity.ok(ApiResponse.success(allNotifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get notifications: " + e.getMessage()));
        }
    }

    /**
     * Get unread notifications for a user
     */
    @Operation(summary = "Get unread notifications for a user",
               description = "Retrieves all unread notifications for a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread notifications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable Integer userId) {
        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(unreadNotifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get unread notifications: " + e.getMessage()));
        }
    }

    /**
     * Mark all notifications as read for a user
     */
    @Operation(summary = "Mark all notifications as read for a user",
               description = "Marks all notifications as read for a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All notifications marked as read successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable Integer userId) {
        try {
            notificationService.markAllNotificationsAsRead(userId);
            return ResponseEntity.ok(ApiResponse.success(null, "All notifications marked as read"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to mark all notifications as read: " + e.getMessage()));
        }
    }

    /**
     * Count unread notifications for a user
     */
    @Operation(summary = "Count unread notifications for a user",
               description = "Returns the count of unread notifications for a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread count retrieved successfully",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(schema = @Schema(implementation = com.no99.edunexusnotification_and_communication.dto.ApiResponse.class)))
    })
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<ApiResponse<Integer>> getUnreadCount(
            @Parameter(description = "User ID", required = true, example = "123")
            @PathVariable Integer userId) {
        try {
            int count = notificationService.countUnreadNotifications(userId);
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get unread count: " + e.getMessage()));
        }
    }
}
