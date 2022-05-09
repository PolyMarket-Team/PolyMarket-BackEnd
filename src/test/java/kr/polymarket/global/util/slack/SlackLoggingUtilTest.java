package kr.polymarket.global.util.slack;

import kr.polymarket.global.config.AsyncConfig;
import kr.polymarket.global.config.PropertiesConfig;
import kr.polymarket.global.util.slack.model.SlackLoggingTargetType;
import kr.polymarket.global.util.slack.model.SlackLoggingType;
import kr.polymarket.global.util.slack.model.SlackWebhookCustomField;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SlackLoggingUtil.class, RestTemplateAutoConfiguration.class, RestTemplate.class, AsyncConfig.class,
    AsyncConfig.class, Executor.class, JacksonAutoConfiguration.class, PropertiesConfig.class,
        SlackLoggingTargetType.SlackWebhookChannelPropertyInjector.class})
@Slf4j
public class SlackLoggingUtilTest {

    @Autowired
    private SlackLoggingUtil slackLoggingUtil;

    @Autowired
    private AsyncListenableTaskExecutor threadPoolTaskExecutor;

    private final static String SLACK_WEBHOOK_SUCCESS_RESPONSE_TEXT = "ok";

    @Test
    @Description("slack message 전송 성공 테스트")
    void 슬랙_메시지_전송_성공_테스트() throws Exception {
        //given
        List<SlackWebhookCustomField> slackWebhookCustomFieldsList = List.of(
                SlackWebhookCustomField.builder()
                        .label("Request IP Address")
                        .text("127.0.0.1")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Host")
                        .text("test.polymarket.kr")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Request URI")
                        .text("/test/uri")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Error Log")
                        .text("this is a test error msg")
                        .build()
        );
        
        // when
        String apiWebhookResponseMessage = slackLoggingUtil.logToSlackWebhookChannel(SlackLoggingTargetType.API, SlackLoggingType.INFO,
                slackWebhookCustomFieldsList);
        String frontWebhookResponseMessage = slackLoggingUtil.logToSlackWebhookChannel(SlackLoggingTargetType.FRONT, SlackLoggingType.INFO,
                slackWebhookCustomFieldsList);
        String mobileWebhookResponseMessage = slackLoggingUtil.logToSlackWebhookChannel(SlackLoggingTargetType.MOBILE, SlackLoggingType.INFO,
                slackWebhookCustomFieldsList);

        // then
        assertThat(apiWebhookResponseMessage).isEqualTo(SLACK_WEBHOOK_SUCCESS_RESPONSE_TEXT);
        assertThat(frontWebhookResponseMessage).isEqualTo(SLACK_WEBHOOK_SUCCESS_RESPONSE_TEXT);
        assertThat(mobileWebhookResponseMessage).isEqualTo(SLACK_WEBHOOK_SUCCESS_RESPONSE_TEXT);
    }

    @Test
    @Description("slack message 비동기 전송 테스트")
    void 슬랙_메시지_비동기_전송_테스트() throws Exception {
        // given
        List<SlackWebhookCustomField> slackWebhookCustomFieldsList = List.of(
                SlackWebhookCustomField.builder()
                        .label("Request IP Address")
                        .text("127.0.0.1")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Host")
                        .text("test.polymarket.kr")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Request URI")
                        .text("/test/uri")
                        .build(),
                SlackWebhookCustomField.builder()
                        .label("Error Log")
                        .text("this is a test error msg")
                        .build()
        );
        final int REQUEST_NUM = 100;
        CountDownLatch countDownLatch = new CountDownLatch(REQUEST_NUM);

        // when
        IntStream.range(0, REQUEST_NUM)
                .forEach(i -> {
                    threadPoolTaskExecutor.submitListenable(() -> {
                        String response = slackLoggingUtil.logToSlackWebhookChannel(SlackLoggingTargetType.API, SlackLoggingType.INFO,
                                slackWebhookCustomFieldsList);
                        countDownLatch.countDown();
                        return response;
                    });
                });

        // then
        boolean isAllCompleted = countDownLatch.await(10, TimeUnit.SECONDS);
        assertThat(isAllCompleted).isTrue();
    }

}
