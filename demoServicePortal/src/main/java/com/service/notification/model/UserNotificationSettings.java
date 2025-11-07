package com.service.notification.model;

import com.service.auth.model.UserRole;

import jakarta.persistence.*;

@Entity
@Table(name = "user_notification_settings")
public class UserNotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Configuración por rol
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", nullable = false, unique = true)
    private UserRole userRole;

    // desactivar correo
    @Column(nullable = false)
    private boolean emailEnabled = true;

    // desactivar notificaciones push
    @Column(nullable = false)
    private boolean pushEnabled = true;

    // activar promociones
    @Column(nullable = false)
    private boolean promotions = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public boolean isEmailEnabled() {
		return emailEnabled;
	}

	public void setEmailEnabled(boolean emailEnabled) {
		this.emailEnabled = emailEnabled;
	}

	public boolean isPushEnabled() {
		return pushEnabled;
	}

	public void setPushEnabled(boolean pushEnabled) {
		this.pushEnabled = pushEnabled;
	}

	public boolean isPromotions() {
		return promotions;
	}

	public void setPromotions(boolean promotions) {
		this.promotions = promotions;
	}
}
