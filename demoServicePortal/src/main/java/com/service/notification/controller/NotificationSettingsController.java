package com.service.notification.controller;

import com.service.notification.dto.UserNotificationSettingsDto;
import com.service.notification.service.UserNotificationSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification/settings")
public class NotificationSettingsController {

    private final UserNotificationSettingsService settingsService;

    public NotificationSettingsController(UserNotificationSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    // ✅ GET /notification/settings/{userRoleId}
    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserNotificationSettingsDto> get(@PathVariable Long userRoleId) {
        var settings = settingsService.getOrCreate(userRoleId);
        return ResponseEntity.ok(UserNotificationSettingsDto.fromEntity(settings));
    }

    // ✅ PUT /notification/settings/{userRoleId}
    @PutMapping("/{userRoleId}")
    public ResponseEntity<UserNotificationSettingsDto> update(
            @PathVariable Long userRoleId,
            @RequestBody UserNotificationSettingsDto body) {

        var updated = settingsService.update(userRoleId, body);
        return ResponseEntity.ok(UserNotificationSettingsDto.fromEntity(updated));
    }
}
