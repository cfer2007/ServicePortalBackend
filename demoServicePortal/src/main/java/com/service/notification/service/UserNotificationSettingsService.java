package com.service.notification.service;

import com.service.auth.model.UserRole;
import com.service.auth.repository.UserRoleRepository;
import com.service.notification.dto.UserNotificationSettingsDto;
import com.service.notification.model.UserNotificationSettings;
import com.service.notification.repository.UserNotificationSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationSettingsService {

    private final UserNotificationSettingsRepository settingsRepo;
    private final UserRoleRepository userRoleRepo;

    public UserNotificationSettingsService(
            UserNotificationSettingsRepository settingsRepo,
            UserRoleRepository userRoleRepo) {
        this.settingsRepo = settingsRepo;
        this.userRoleRepo = userRoleRepo;
    }

    // ✅ Obtener o crear configuración por userRoleId
    public UserNotificationSettings getOrCreate(Long userRoleId) {
        return settingsRepo.findByUserRole_UserRoleId(userRoleId)
                .orElseGet(() -> {
                    UserRole role = userRoleRepo.findById(userRoleId)
                            .orElseThrow(() -> new RuntimeException("UserRole no encontrado"));

                    UserNotificationSettings settings = new UserNotificationSettings();
                    settings.setUserRole(role);
                    settings.setEmailEnabled(true);
                    settings.setPromotions(true);
                    return settingsRepo.save(settings);
                });
    }

    // ✅ Actualizar configuración usando DTO
    public UserNotificationSettings update(Long userRoleId, UserNotificationSettingsDto dto) {
        UserNotificationSettings current = getOrCreate(userRoleId);
        dto.applyToEntity(current);
        return settingsRepo.save(current);
    }
}
