package org.matrix.izumbankapp.dao.repository;

import org.matrix.izumbankapp.dao.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<List<Notification>> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Notification notif WHERE notif.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
