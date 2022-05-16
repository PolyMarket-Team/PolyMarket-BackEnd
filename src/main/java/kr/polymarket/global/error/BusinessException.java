package kr.polymarket.global.error;

public class BusinessException extends ErrorCodeException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}