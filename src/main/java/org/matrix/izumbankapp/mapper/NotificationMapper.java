package org.matrix.izumbankapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.matrix.izumbankapp.dao.entities.NotificationEntity;
import org.matrix.izumbankapp.model.notifications.NotificationRequest;
import org.matrix.izumbankapp.model.notifications.NotificationResponse;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    @Mapping(source = "notificationEntity.user.id", target = "userId")
    NotificationResponse toDto(NotificationEntity notificationEntity);

    @Mapping(source = "notificationRequest.userId", target = "user.id")
    NotificationEntity toEntity(NotificationRequest notificationRequest);
}
