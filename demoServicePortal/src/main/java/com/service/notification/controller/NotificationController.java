package com.service.notification.controller;

import com.service.notification.dto.NotificationDto;
import com.service.notification.dto.NotificationRequestDto;
import com.service.notification.model.Notification;
import com.service.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ✅ Enviar / crear notificación
    @PostMapping("/send")
    public ResponseEntity<NotificationDto> send(@RequestBody NotificationRequestDto req) {
        Notification n = notificationService.sendNotification(
                req.getUserRoleId(),
                req.getTitle(),
                req.getMessage(),
                req.getType()
        );
        return ResponseEntity.ok(NotificationDto.fromEntity(n));
    }

    // ✅ Obtener todas las notificaciones del rol (como DTO)
    @GetMapping("/{userRoleId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long userRoleId) {
        List<Notification> list = notificationService.getNotifications(userRoleId);
        List<NotificationDto> dtos = list.stream()
                .map(NotificationDto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // ✅ Contar cuántas no leídas tiene ese rol
    @GetMapping("/count/{userRoleId}")
    public ResponseEntity<Long> countUnread(@PathVariable Long userRoleId) {
        return ResponseEntity.ok(notificationService.countUnread(userRoleId));
    }

    // ✅ Marcar todas como leídas
    @PostMapping("/read-all/{userRoleId}")
    public ResponseEntity<String> markAllRead(@PathVariable Long userRoleId) {
        int updated = notificationService.markAllRead(userRoleId);
        return ResponseEntity.ok("Notificaciones marcadas como leídas: " + updated);
    }
    
    @PostMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

}
