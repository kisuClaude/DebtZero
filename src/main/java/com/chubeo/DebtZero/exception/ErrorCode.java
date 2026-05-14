package com.chubeo.DebtZero.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR),

    EMAIL_EXISTED(1001, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1002, "Phone number already exists", HttpStatus.BAD_REQUEST),
    USER_EXISED(1003, "Username already exists", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Authentication required", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1005, "User not found", HttpStatus.NOT_FOUND),

    TOKEN_EXPIRED(2001, "Refresh token has expired", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(2002, "Refresh token not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(2003, "Invalid username or password", HttpStatus.BAD_REQUEST),

    ROLE_NOT_FOUND(3001, "Role not found", HttpStatus.NOT_FOUND),

    DEBT_NOT_FOUND(4001, "Debt not found", HttpStatus.NOT_FOUND),
    DEBT_HAS_PAID(4002, "This debt has already been paid", HttpStatus.BAD_REQUEST)



    ;



    private final String message;
    private final int code;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
