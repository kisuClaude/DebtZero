package com.chubeo.DebtZero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
