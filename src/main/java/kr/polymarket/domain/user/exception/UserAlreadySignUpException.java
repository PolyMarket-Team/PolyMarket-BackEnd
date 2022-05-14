package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class UserAlreadySignUpException extends BusinessException {

    public UserAlreadySignUpException() {
        super(null, ErrorCode.USER_ALREADY_SIGNUP);
    }

    public UserAlreadySignUpException(String message) {
        super(message, ErrorCode.USER_ALREADY_SIGNUP);
    }
}
