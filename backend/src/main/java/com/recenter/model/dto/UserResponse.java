package com.recenter.model.dto;

import com.recenter.model.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;
}
