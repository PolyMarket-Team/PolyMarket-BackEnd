package kr.polymarket.domain.user.exception;

import kr.polymarket.global.error.BusinessException;
import kr.polymarket.global.error.ErrorCode;

public class NicknameAlreadyExistsException extends BusinessException {

    public NicknameAlreadyExistsException() {
        super("", ErrorCode.NICKNAME_ALREADY_EXISTS);
    }

    public NicknameAlreadyExistsException(String message) {
        super(message, ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}
