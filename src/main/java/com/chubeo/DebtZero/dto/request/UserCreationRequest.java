package com.chubeo.DebtZero.dto.request;

import lombok.Data;

@Data
public class UserCreationRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
