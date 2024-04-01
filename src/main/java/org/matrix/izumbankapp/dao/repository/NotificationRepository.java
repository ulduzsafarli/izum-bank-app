package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Optional<List<NotificationEntity>> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
