package com.recenter.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    private String firstName;
    private String lastName;
    private String middleName;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phoneNumber;
}
