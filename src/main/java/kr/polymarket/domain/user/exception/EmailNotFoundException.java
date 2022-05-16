package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class EmailNotFoundException extends BusinessException {

    public EmailNotFoundException() {
        super(ErrorCode.EMAIL_AUTH_NOT_FOUND);
    }

    public EmailNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.EMAIL_AUTH_NOT_FOUND);
    }
}
