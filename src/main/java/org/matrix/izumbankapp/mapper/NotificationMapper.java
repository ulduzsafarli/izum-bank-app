package org.matrix.izumbankapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.matrix.izumbankapp.dao.entities.Notification;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    @Mapping(source = "notification.user.id", target = "userId")
    NotificationResponse toDto(Notification notification);

    @Mapping(source = "notificationRequest.userId", target = "user.id")
    Notification toEntity(NotificationRequest notificationRequest);
}
