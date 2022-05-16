package kr.polymarket.global.error;

public class ForbiddenException extends ErrorCodeException {

    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}
