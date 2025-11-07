package com.service.notification.model;

import java.time.LocalDateTime;

import com.service.auth.model.UserRole;
import com.service.notification.enums.NotificationType;
import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // A quién va dirigida la notificación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type; // SYSTEM, PROMOTION, APPOINTMENT, CHAT, etc.

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "sent_email")
    private boolean sentEmail; // si también se mandó correo

    @Column(name = "sent_push")
    private boolean sentPush; // si se mandó notificación push

    @Column(nullable = false)
    private boolean readed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSentEmail() {
		return sentEmail;
	}

	public void setSentEmail(boolean sentEmail) {
		this.sentEmail = sentEmail;
	}

	public boolean isSentPush() {
		return sentPush;
	}

	public void setSentPush(boolean sentPush) {
		this.sentPush = sentPush;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
