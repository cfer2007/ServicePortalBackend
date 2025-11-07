package com.service.notification.dto;

import com.service.notification.model.UserNotificationSettings;

public class UserNotificationSettingsDto {

    private Long userRoleId;
    private boolean emailEnabled;
    private boolean pushEnabled;
    private boolean promotions;
    
    public UserNotificationSettingsDto() {}

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
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

    public static UserNotificationSettingsDto fromEntity(UserNotificationSettings s) {
        UserNotificationSettingsDto dto = new UserNotificationSettingsDto();
        dto.setUserRoleId(s.getUserRole().getUserRoleId());
        dto.setEmailEnabled(s.isEmailEnabled());
        dto.setPromotions(s.isPromotions());
        dto.setPushEnabled(s.isPushEnabled());
        return dto;
    }

    public void applyToEntity(UserNotificationSettings entity) {
        entity.setEmailEnabled(this.emailEnabled);
        entity.setPushEnabled(this.pushEnabled);
        entity.setPromotions(this.promotions);
    }
}
