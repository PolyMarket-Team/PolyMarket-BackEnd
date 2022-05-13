package kr.polymarket.domain.user.controller;

import io.swagger.annotations.*;
import kr.polymarket.domain.user.dto.EmailAuthRequestDto;
import kr.polymarket.domain.user.dto.EmailAuthResponseDto;
import kr.polymarket.domain.user.dto.EmailAuthCheckRequestDto;
import kr.polymarket.domain.user.service.EmailAuthService;
import kr.polymarket.global.error.ErrorResponse;
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
    @ApiOperation(value = "이메일 인증코드 전송 API", notes = "회원가입전 이메일 인증코드를 이메일로 보내주는 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success send auth code to email"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
    })
    @PostMapping("/send-email")
    public ResponseEntity<ResultResponse<EmailAuthResponseDto>> sendEmailAuthCode(@Valid @RequestBody EmailAuthRequestDto emailAuthRequestDto) {
        EmailAuthResponseDto emailAuthResult = emailAuthService.sendAuthCodeToEmail(emailAuthRequestDto);

        ResultResponse<EmailAuthResponseDto> result = ResultResponse.of(ResultCode.EMAIL_SEND_SUCCESS, emailAuthResult);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    /**
     * 이메일 인증요청
     * @param emailCodeRequestDto
     */
    @PostMapping("/confirm-email")
    @ApiOperation(value = "이메일 인증코드 확인 API", notes = "회원가입전 이메일로 전승된 인증코드로 이메일을 인증하는 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success confirm auth code for email"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
    })
    public ResponseEntity<ResultResponse<Void>> confirmEmailAuthCode(@Valid @RequestBody EmailAuthCheckRequestDto emailCodeRequestDto) {
        emailAuthService.confirmEmailAuthCode(emailCodeRequestDto);

        ResultResponse<Void> result = ResultResponse.of(ResultCode.CONFIRM_EMAIL_SUCCESS);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
