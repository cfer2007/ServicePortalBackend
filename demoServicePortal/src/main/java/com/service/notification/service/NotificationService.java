package com.service.notification.service;

import com.service.notification.model.Notification;
import com.service.notification.enums.NotificationType;
import com.service.notification.repository.NotificationRepository;
import com.service.auth.model.UserRole;
import com.service.auth.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRoleRepository userRoleRepo;

    public NotificationService(NotificationRepository notificationRepo, UserRoleRepository userRoleRepo) {
        this.notificationRepo = notificationRepo;
        this.userRoleRepo = userRoleRepo;
    }

    // ✅ Crear nueva notificación
    public Notification sendNotification(Long userRoleId, String title, String message, NotificationType type) {
        UserRole role = userRoleRepo.findById(userRoleId).orElseThrow(() -> new RuntimeException("UserRole no encontrado"));
        Notification n = new Notification();
        n.setUserRole(role);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setReaded(false);
        n.setCreatedAt(LocalDateTime.now());
        return notificationRepo.save(n);
    }

    // ✅ Obtener notificaciones por rol
    public List<Notification> getNotifications(Long userRoleId) {
        return notificationRepo.findByUserRole_UserRoleIdOrderByCreatedAtDesc(userRoleId);
    }

    // ✅ Contar no leídas
    public long countUnread(Long userRoleId) {
        return notificationRepo.countByUserRole_UserRoleIdAndReadedFalse(userRoleId);
    }

    // ✅ Marcar todas como leídas
    @Transactional
    public int markAllRead(Long userRoleId) {
        return notificationRepo.markAllAsRead(userRoleId);
    }
    
    public void markAsRead(Long id) {
        Notification n = notificationRepo.findById(id).orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));
        n.setReaded(true);
        notificationRepo.save(n);
    }

}
