package org.matrix.izumbankapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;
import org.matrix.izumbankapp.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/{userId}/notification")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getByUserId(@PathVariable Long userId) {
        return notificationService.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody NotificationRequest notificationRequest) {
        notificationService.create(notificationRequest);
    }

    @DeleteMapping("/{userId}/notification")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@PathVariable Long userId) {
        notificationService.deleteUserId(userId);
    }
}
