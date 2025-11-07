package com.service.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.service.notification.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserRole_UserRoleIdOrderByCreatedAtDesc(Long userRoleId);

    long countByUserRole_UserRoleIdAndReadedFalse(Long userRoleId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.readed = true WHERE n.userRole.userRoleId = :userRoleId AND n.readed = false")
    int markAllAsRead(Long userRoleId);
}
