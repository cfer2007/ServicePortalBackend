package com.service.notification.dto;

import com.service.notification.enums.NotificationType;

public class NotificationRequestDto {
    private Long userRoleId;
    private String title;
    private String message;
    private NotificationType type;

    public NotificationRequestDto() {}

    public Long getUserRoleId() { return userRoleId; }
    public void setUserRoleId(Long userRoleId) { this.userRoleId = userRoleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
}
