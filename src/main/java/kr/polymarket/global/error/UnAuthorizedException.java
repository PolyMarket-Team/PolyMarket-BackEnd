package kr.polymarket.global.error;

public class UnAuthorizedException extends ErrorCodeException {

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }

}
