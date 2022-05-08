package kr.polymarket.global.error;

import kr.polymarket.global.util.slack.SlackLoggingUtil;
import kr.polymarket.global.util.slack.model.SlackLoggingTargetType;
import kr.polymarket.global.util.slack.model.SlackLoggingType;
import kr.polymarket.global.util.slack.model.SlackWebhookCustomField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import static kr.polymarket.global.error.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final Executor executor;
    private final SlackLoggingUtil slackLoggingUtil;

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getParameterName());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getConstraintViolations());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getBindingResult());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getBindingResult());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getRequestPartName());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        final ErrorResponse response = ErrorResponse.of(HTTP_MESSAGE_NOT_READABLE);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final List<ErrorResponse.FieldError> errors = new ArrayList<>();
        errors.add(new ErrorResponse.FieldError("http method", e.getMethod(), METHOD_NOT_ALLOWED.getMessage()));
        final ErrorResponse response = ErrorResponse.of(HTTP_HEADER_INVALID, errors);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("{} {} {} {}",request.getRemoteHost(), request.getRemoteAddr(), request.getRequestURI(),
                request.getHeader("X-Forwarded-For"), e);

        sendErrorMessageToSlackWebhookChannel(e, request);

        final ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * send to slack api logging noti channel
     * @param e
     */
    private void sendErrorMessageToSlackWebhookChannel(Exception e, HttpServletRequest request) {
        try {
            List<SlackWebhookCustomField> slackWebhookCustomFieldsList = List.of(
                    SlackWebhookCustomField.builder()
                            .label("Request IP Address")
                            .text(request.getHeader("X-Forwarded-For") == null? request.getRemoteAddr() : request.getHeader("X-Forwarded-For") + "(proxy by nginx)")
                            .build(),
                    SlackWebhookCustomField.builder()
                            .label("Request URL")
                            .text(request.getRequestURL().toString())
                            .build(),
                    SlackWebhookCustomField.builder()
                            .label("Error Log")
                            .text(extractErrorMessage(e))
                            .build()
            );

            executor.execute(() -> {
                try {
                    slackLoggingUtil.logToSlackWebhookChannel(SlackLoggingTargetType.API, SlackLoggingType.ERROR, slackWebhookCustomFieldsList);
                } catch (Exception ex) {
                    // TODO 429(too many requests 메시지는 논의)
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    /**
     * 슬랙 웹훅으로 보낼 Exception 메시지 추출
     * @param e
     * @return
     */
    private String extractErrorMessage(Exception e) {
        final int SLACK_WEBHOOK_TEXT_LENGTH_LIMIT = 2000; // slack message text 글자제한수
        StringBuilder slackMessageBuilder = new StringBuilder();
        AtomicInteger messageLineBreakNum = new AtomicInteger(0); // java 문자열은 \n을 하나의 문자로 취급하기 때문에 줄바꿈 문자 갯수 카운팅하기 위한 변수
        slackMessageBuilder
                .append(e.getClass().getName())
                .append("\n")
                .append(e.getMessage())
                .append("\n");
        Arrays.stream(e.getStackTrace())
                .forEach(stackTraceElement -> {
                    messageLineBreakNum.getAndIncrement();
                    slackMessageBuilder
                            .append(stackTraceElement.toString())
                            .append("\n");
                });
        messageLineBreakNum.addAndGet(2); // 위 append 작업에서 두개의 줄바꿈문자('\n')를 추가했으므로 카운트 증가

        /**
         * slack text 메시지는 2000자 글자수 제한이 있어 substring 연산 및 결과 반환
         * ! 자바에서는 줄바꿈문자('\n')을 하나의 문자로 취급하지만 json 문자열 과정에서 '\', 'n' 문자를 별개의 문자로 보기때문에 줄바꿈 문자갯수를 빼준다
         */
        return slackMessageBuilder
                .substring(0, Math.min(slackMessageBuilder.length(), SLACK_WEBHOOK_TEXT_LENGTH_LIMIT) - messageLineBreakNum.get());
    }

}