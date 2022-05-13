package kr.polymarket.domain.user.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.polymarket.domain.user.dto.*;
import kr.polymarket.domain.user.service.UserAuthService;
import kr.polymarket.domain.user.service.UserService;
import kr.polymarket.global.error.ErrorResponse;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;

    @ApiOperation(value = "회원가입 API", notes = "회원가입 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success signup"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized (email is unauthorized)", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "conflict (duplicated nickname or user already exists)", response = ErrorResponse.class),
    })
    @PostMapping("/signup")
    public ResponseEntity<ResultResponse<Void>> signUp(@Valid @RequestBody SignUpRequestDto signUpDto) {
        userService.createUser(signUpDto);

        ResultResponse<Void> result = ResultResponse.of(ResultCode.SIGNUP_SUCCESS, null);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @ApiOperation(value = "로그인 API", notes = "로그인 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success signup"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized (email is unauthorized)", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "conflict (duplicated nickname or user already exists)", response = ErrorResponse.class),
    })
    @PostMapping("/signin")
    public ResponseEntity<ResultResponse<LoginResponseDto>> signIn(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userAuthService.userSignIn(loginRequestDto);

        ResultResponse<LoginResponseDto> result = ResultResponse.of(ResultCode.SIGNIN_SUCCESS, loginResponseDto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @ApiOperation(value = "토큰 refresh API", notes = "refresh token으로 새로운 access token과 refresh token을 발급하는 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success signup"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized (token is invalid)", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "not found", response = ErrorResponse.class),
    })
    @PostMapping("/refresh")
    public ResponseEntity<ResultResponse<TokenResponseDto>> refresh(@RequestHeader("X-REFRESH-TOKEN") String refreshToken) {
        TokenResponseDto tokenResponseDto = userAuthService.tokenRefresh(refreshToken);

        ResultResponse<TokenResponseDto> result = ResultResponse.of(ResultCode.REFRESH_SUCCESS, tokenResponseDto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @ApiOperation(value = "회원 프로필 조회 API", notes = "회원의 프로필 정보, 닉네임,  API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success user profile"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "not found", response = ErrorResponse.class),
    })
    @PostMapping("/{userId}")
    public ResponseEntity<ResultResponse<UserProfileResponse>> userProfile(@PathVariable long userId) {
        UserProfileResponse userProfileResponse = userService.findUserProfile(userId);
        ResultResponse<UserProfileResponse> result = ResultResponse.of(ResultCode.SUCCESS, userProfileResponse);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
