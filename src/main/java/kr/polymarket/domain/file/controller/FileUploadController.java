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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    /**
     * 회원 프로필 이미지 업로드
     * @param multipartFile
     * @return fileId
     */
    @ApiOperation(value = "회원 프로필 이미지 업로드 API", notes = "회원 프로필 정보(파일 ID)API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success upload user profile image"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class),
    })
    @PostMapping("/upload/profileimage")
    public ResponseEntity<ResultResponse<Long>> uploadImage(@RequestPart("image") MultipartFile multipartFile) {
        Long fileId = imageUploadService.uploadUserProfileImage(multipartFile);

        ResultResponse<Long> result = ResultResponse.of(ResultCode.IMAGE_UPLOAD_SUCCESS, fileId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    /**
     * 상품 이미지 업로드
     * @param multipartFile
     * @return fileUrlList
     */
    @ApiOperation(value = "상품 이미지 업로드 API", notes = "상품 파일 이미지API", produces = "multipart/form-data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success upload proudct image"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class),
    })
    @PostMapping(value = "/upload/productimage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResultResponse<List>> uploadProductImage(@RequestPart("image") List<MultipartFile> multipartFile) {
        List fileUrlList = imageUploadService.uploadProductImage(multipartFile);

        ResultResponse<List> result = ResultResponse.of(ResultCode.IMAGE_UPLOAD_SUCCESS, fileUrlList);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}

