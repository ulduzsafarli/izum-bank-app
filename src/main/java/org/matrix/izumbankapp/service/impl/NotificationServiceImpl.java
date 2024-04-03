package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.dao.repository.NotificationRepository;
import org.matrix.izumbankapp.exception.NotFoundException;
import org.matrix.izumbankapp.mapper.NotificationMapper;
import org.matrix.izumbankapp.model.NotificationRequest;
import org.matrix.izumbankapp.model.NotificationResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.service.EmailSendingService;
import org.matrix.izumbankapp.service.NotificationService;
import org.matrix.izumbankapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final String NOT_FOUND = "Notification %s by ID not found.";

    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final EmailSendingService emailSendingService;

    @Override
    @Transactional
    public ResponseDto createNotification(NotificationRequest notificationRequest) {
        log.info("Creating notification for user {}", notificationRequest.getUserId());
        var user = userService.getUserById(notificationRequest.getUserId());
        var notification = notificationMapper.toEntity(notificationRequest);
        notification.setSentDate(LocalDate.now());
        notificationRepository.save(notification);
        var emailForm = new EmailAnswerDto(notificationRequest.getMessage());
        emailSendingService.sendNotificationEmail(user.getEmail(), emailForm);
        log.info("Successfully create notification for user {}", notificationRequest.getUserId());
        return ResponseDto.builder().responseMessage("Successfully send user notification ").build();
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        log.info("Receiving all notifications");
        var notifications = notificationRepository.findAll()
                .stream().map(notificationMapper::toDto).toList();
        log.info("Successfully receive all notifications");
        return notifications;
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        log.info("Receiving all notifications for user {}", userId);
        var notifications = notificationRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, userId)))
                .stream().map(notificationMapper::toDto).toList();
        log.info("Successfully receive all notifications");
        return notifications;
    }

    @Override
    @Transactional
    public ResponseDto deleteNotificationsByUserId(Long userId) {
        log.info("Removing all notifications for user {}", userId);

        int deletedCount = notificationRepository.deleteByUserId(userId);

        log.info("Successfully removed {} notifications for the user {}", deletedCount, userId);
        return ResponseDto.builder()
                .responseMessage("Notifications for the user deleted successfully")
                .build();
    }





}
