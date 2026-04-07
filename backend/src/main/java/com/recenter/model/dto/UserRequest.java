package com.recenter.model.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
}
