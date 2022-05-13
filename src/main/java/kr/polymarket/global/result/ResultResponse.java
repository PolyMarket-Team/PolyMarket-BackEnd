package kr.polymarket.global.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ResultResponse<R> {

    @ApiModelProperty(name = "응답상태", notes = "http 응답상태코드", example = "200")
    private final int status;

    @ApiModelProperty(name = "응답코드", notes = "응답코드", example = "C999")
    private final String code;

    @ApiModelProperty(name = "응답메시지", notes = "응답메시지", example = "response message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @ApiModelProperty(name = "응답데이터", notes = "응답데이터", example = "response data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final R data;

    public static<T> ResultResponse<T> of(ResultCode resultCode, T data) {
        return new ResultResponse<>(resultCode, data);
    }

    public static ResultResponse<Void> of(ResultCode resultCode) {
        return new ResultResponse<>(resultCode, null);
    }

    public ResultResponse(ResultCode resultCode, R data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

}