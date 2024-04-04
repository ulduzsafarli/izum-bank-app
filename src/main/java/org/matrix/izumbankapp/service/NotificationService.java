package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    ResponseDto createNotification(NotificationRequest notificationRequest);

    List<NotificationResponse> getAllNotifications();

    List<NotificationResponse> getNotificationsByUserId(Long userId);

    ResponseDto deleteNotificationsByUserId(Long userId);
}
