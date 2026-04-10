package com.finanscepte.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "username bos olamaz") String username,
        @Email(message = "email formati gecersiz") @NotBlank(message = "email bos olamaz") String email
) {
}
