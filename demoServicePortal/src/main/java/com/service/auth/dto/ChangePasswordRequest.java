package com.service.auth.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {}