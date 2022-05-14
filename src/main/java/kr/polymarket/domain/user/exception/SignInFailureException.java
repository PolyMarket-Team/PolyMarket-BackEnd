package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class SignInFailureException extends BusinessException {

    public SignInFailureException() {
        super(null, ErrorCode.SIGN_IN_FAIL);
    }

    public SignInFailureException(String message) {
        super(message, ErrorCode.SIGN_IN_FAIL);
    }
}
