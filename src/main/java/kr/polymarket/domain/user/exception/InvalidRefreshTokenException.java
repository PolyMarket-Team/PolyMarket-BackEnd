package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException() {
        super(null, ErrorCode.REFRESH_TOKEN_FAIL);
    }

    public InvalidRefreshTokenException(String message) {
        super(message, ErrorCode.REFRESH_TOKEN_FAIL);
    }
}
