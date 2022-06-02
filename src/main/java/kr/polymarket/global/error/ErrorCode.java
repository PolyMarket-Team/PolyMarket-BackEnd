package kr.polymarket.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    UNAUTHORIZED(401, "C001", "인증되지 않았습니다."),
    FORBIDDEN(403, "C002", "접권권한이 없습니다."),
    FILE_NOT_FOUND(404, "C003", "파일을 찾을 수 없습니다."),

    // Product
    SEARCH_NOT_FOUND(404, "P001","검색 컨텍스트를 찾을 수 없습니다."),

    // User
    USER_ALREADY_SIGNUP(409, "U001", "이미 가입한 사용자입니다."),
    EMAIL_AUTH_CODE_AUTH_FAIL(401, "U002", "이메일 인증에 실패하였습니다."),
    EMAIL_AUTH_NOT_FOUND(404, "U003", "찾을 수 없는 이메일 인증정보입니다."),
    SIGN_IN_FAIL(401, "U004", "로그인에 실패하였습니다."),
    REFRESH_TOKEN_FAIL(401, "U004", "access token 재발행에 실패했습니다."),
    NICKNAME_ALREADY_EXISTS(409, "U005", "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(404, "U006", "탈퇴하거나 존재하지않는 회원입니다."),

    // Product
    PRODUCT_NOT_FOUND(403, "D001", "상품을 찾을 수 없습니다"),
    PRODUCTFILE_SIZE_NOT_CORRESPOND(400, "D002", "파일 사이즈가 일치하지 않습니다."),

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
