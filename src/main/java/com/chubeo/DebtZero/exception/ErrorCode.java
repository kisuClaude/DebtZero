package com.chubeo.DebtZero.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1001, "Email existed", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1002, "Phone number existed", HttpStatus.BAD_REQUEST),
    USER_EXISED(1003, "User existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),

    TOKEN_EXPIRED(2001, "Token has expired", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(2002, "Token not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(2003, "Invalid token", HttpStatus.BAD_REQUEST),

    ROLE_NOT_FOUND(3001, "Role not found", HttpStatus.NOT_FOUND),

    DEBT_NOT_FOUND(4001, "Debt not found or you are not in debt", HttpStatus.NOT_FOUND)



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
