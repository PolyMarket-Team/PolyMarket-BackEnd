package kr.polymarket.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    EMAIL_SEND_SUCCESS(200,"M000", "이메일전송에 성공하였습니다"),
    SIGNUP_SUCCESS(200, "M001", "회원가입 되었습니다."),
    SIGNIN_SUCCESS(200, "m002", "로그인에 성공하였습니다."),
    CONFIRM_EMAIL_SUCCESS(200, "M003", "이메일 인증에 성공하셨습니다."),
    REFRESH_SUCCESS(200, "M004", "토큰 재발급에 성공하셨습니다.");

    private int status;
    private final String code;
    private final String message;
}
