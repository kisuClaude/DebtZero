package com.chubeo.DebtZero.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
}
