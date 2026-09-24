package com.likelion.NetflixClone.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
}
