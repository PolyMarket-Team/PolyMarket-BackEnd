package kr.polymarket.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_ALREADY_SIGNUP(409, "U001", "이미 가입한 사용자입니다."),
    EMAIL_AUTH_CODE_AUTH_FAIL(401, "U002", "이메일 인증에 실패하였습니다."),
    EMAIL_AUTH_NOT_FOUND(404, "U003", "찾을 수 없는 이메일 인증정보입니다."),

    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "G003", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G004", "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
    IMAGE_TYPE_NOT_SUPPORTED(400, "G007", "지원하지 않는 이미지 타입입니다."),
    ;

    private int status;
    private final String code;
    private final String message;
}
