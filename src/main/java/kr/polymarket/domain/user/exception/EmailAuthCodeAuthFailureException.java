package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class EmailAuthCodeAuthFailureException extends BusinessException {

    public EmailAuthCodeAuthFailureException() {
        super("", ErrorCode.EMAIL_AUTH_CODE_AUTH_FAIL);
    }

    public EmailAuthCodeAuthFailureException(String message) {
        super(message, ErrorCode.EMAIL_AUTH_CODE_AUTH_FAIL);
    }
}
