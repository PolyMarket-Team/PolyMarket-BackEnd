package kr.polymarket.global.util.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.polymarket.global.properties.SlackWebhookChannelProperty;
import kr.polymarket.global.util.slack.model.SlackLoggingTargetType;
import kr.polymarket.global.util.slack.model.SlackLoggingType;
import kr.polymarket.global.util.slack.model.SlackWebhookCustomField;
import kr.polymarket.global.util.slack.model.request.SlackWebhookSectionBlockRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackLoggingUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final SlackWebhookChannelProperty slackWebhookChannelProperty;

    /**
     * 슬랙 로깅 채널로 메시지를 보내는 메소드
     *
     * @param slackLoggingTargetType
     * @param slackLoggingType
     * @param slackWebhookCustomFieldsList
     * @return result - status 200(성공)시 "ok" 문자열 반환
     */
    public String logToSlackWebhookChannel(SlackLoggingTargetType slackLoggingTargetType, SlackLoggingType slackLoggingType,
                                           List<SlackWebhookCustomField> slackWebhookCustomFieldsList) throws Exception {
        MultiValueMap<String, String> requestPayload = new LinkedMultiValueMap<>();
        Map<String, List<SlackWebhookSectionBlockRequest>> blockListReq = new HashMap<>();
        SlackWebhookSectionBlockRequest slackRequest = SlackWebhookSectionBlockRequest.builder()
                .type(SlackWebhookSectionBlockRequest.BlockType.section)
                .text(SlackWebhookSectionBlockRequest.BlockElementField.builder()
                        .type(SlackWebhookSectionBlockRequest.BlockElementField.BlockElementType.mrkdwn)
                        .text("*:memo:" + slackLoggingTargetType.name() + " Logging Noti*")
                        .build())
                .fields(createSlackWebhookSectionFields(slackLoggingType, slackWebhookCustomFieldsList))
                .build();
        blockListReq.put("blocks", List.of(slackRequest));
        String slackRequestStr = objectMapper.writeValueAsString(blockListReq);
        requestPayload.add("payload", slackRequestStr);

        ResponseEntity<String> response = restTemplate.postForEntity(
                UriComponentsBuilder.fromHttpUrl(slackLoggingTargetType.getWebhookChannelUrl()).toUriString(),
                requestPayload, String.class);

        return response.getBody();
    }

    /**
     * 슬랙 웹훅으로 보낼 Exception 메시지 추출
     * @param e
     * @return
     */
    public String extractErrorMessage(Throwable e) {
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

    /**
     * webhook 메시지 section 블록내 필드들을 구성하는 메소드
     * @param slackLoggingType
     * @param slackWebhookCustomFieldList
     * @return
     */
    private List<SlackWebhookSectionBlockRequest.BlockElementField> createSlackWebhookSectionFields(SlackLoggingType slackLoggingType,
                                                                                                    List<SlackWebhookCustomField> slackWebhookCustomFieldList) {
        List<SlackWebhookSectionBlockRequest.BlockElementField> slackWebhookSectionBlockElementList = new ArrayList<>();
        slackWebhookSectionBlockElementList.add(
                SlackWebhookSectionBlockRequest.BlockElementField.builder()
                    .type(SlackWebhookSectionBlockRequest.BlockElementField.BlockElementType.mrkdwn)
                    .text("*logging type*\n" + slackLoggingType.name())
                    .build());

        slackWebhookCustomFieldList.forEach(slackWebhookCustomField ->
                slackWebhookSectionBlockElementList.add(
                        SlackWebhookSectionBlockRequest.BlockElementField.builder()
                                .type(SlackWebhookSectionBlockRequest.BlockElementField.BlockElementType.mrkdwn)
                                .text("*" + slackWebhookCustomField.getLabel()+ "*\n" + slackWebhookCustomField.getText())
                                .build())
        );

        return slackWebhookSectionBlockElementList;
    }

}
