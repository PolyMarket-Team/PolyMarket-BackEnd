package kr.polymarket.domain.user.controller;

import kr.polymarket.domain.user.dto.EmailAuthRequestDto;
import kr.polymarket.domain.user.dto.EmailAuthResultDto;
import kr.polymarket.domain.user.dto.EmailCodeRequestDto;
import kr.polymarket.domain.user.service.EmailAuthService;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    /**
     * 이메일 인증코드 전송
     * @param emailAuthRequestDto
     */
    @PostMapping("/send-email")
    public ResponseEntity<ResultResponse> sendEmailAuthCode(@Valid @RequestBody EmailAuthRequestDto emailAuthRequestDto) {
        EmailAuthResultDto emailAuthResult = emailAuthService.sendAuthCodeToEmail(emailAuthRequestDto);

        ResultResponse result = ResultResponse.of(ResultCode.EMAIL_SEND_SUCCESS, emailAuthResult);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    /**
     * 이메일 인증요청
     * @param emailCodeRequestDto
     */
    @PostMapping("/confirm-email")
    public ResponseEntity<ResultResponse> confirmEmailAuthCode(@Valid @RequestBody EmailCodeRequestDto emailCodeRequestDto) {
        emailAuthService.confirmEmailAuthCode(emailCodeRequestDto);

        ResultResponse result = ResultResponse.of(ResultCode.CONFIRM_EMAIL_SUCCESS, emailCodeRequestDto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
