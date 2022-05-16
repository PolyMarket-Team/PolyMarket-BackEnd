package kr.polymarket.domain.file.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.polymarket.domain.file.service.ImageUploadService;
import kr.polymarket.global.error.ErrorResponse;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    @ApiOperation(value = "회원 프로필 이미지 업로드 API", notes = "회원 프로필 정보(파일 ID)API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success upload user profile image"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class),
    })
    @PostMapping("/upload/profile")
    public ResponseEntity<ResultResponse<Long>> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        Long fileId = imageUploadService.uploadUserProfileImage(multipartFile);

        ResultResponse<Long> result = ResultResponse.of(ResultCode.IMAGE_UPLOAD_SUCCESS, fileId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
