package kr.polymarket.global.util.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackLoggingUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final static String API_DEV_LOGGING_CHANNEL_WEBHOOK_URL = "https://hooks.slack.com/services/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";

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

        // TODO slackLoggingTargetType에 따라 알맞은 채널 uri로 요청을 보내도록 코드 추가
        ResponseEntity<String> response = restTemplate.postForEntity(UriComponentsBuilder.fromHttpUrl(API_DEV_LOGGING_CHANNEL_WEBHOOK_URL).toUriString(),
                requestPayload, String.class);

        return response.getBody();

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
