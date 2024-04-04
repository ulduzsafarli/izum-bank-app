package org.matrix.izumbankapp.controller;

import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getAllNotifications());
    }

    @GetMapping("/{userId}/notification")
    public ResponseEntity<List<NotificationResponse>> getAllNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationsByUserId(userId));
    }

    @PostMapping("/notification")
    public ResponseEntity<ResponseDto> getAllNotificationsForUser(@RequestBody NotificationRequest notificationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.createNotification(notificationRequest));
    }

    @DeleteMapping("/{userId}/notification")
    public ResponseEntity<ResponseDto> deleteNotification(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.deleteNotificationsByUserId(userId));
    }
}
