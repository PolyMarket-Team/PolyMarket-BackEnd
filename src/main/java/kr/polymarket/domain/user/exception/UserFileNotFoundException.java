package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class UserFileNotFoundException extends BusinessException {

    public UserFileNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserFileNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
