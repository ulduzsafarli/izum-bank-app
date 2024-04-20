package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void create(NotificationRequest notificationRequest);

    List<NotificationResponse> getAll();

    List<NotificationResponse> getByUserId(Long userId);

    void deleteUserId(Long userId);
}
