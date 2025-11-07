package com.service.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.notification.model.UserNotificationSettings;

import java.util.Optional;

public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSettings, Long> {

    Optional<UserNotificationSettings> findByUserRole_UserRoleId(Long userRoleId);
}
