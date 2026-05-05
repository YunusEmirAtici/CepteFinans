package com.finanscepte.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank(message = "userId bos olamaz") String userId,
        @NotBlank(message = "type bos olamaz") String type,
        @NotBlank(message = "message bos olamaz") String message
) {
}
