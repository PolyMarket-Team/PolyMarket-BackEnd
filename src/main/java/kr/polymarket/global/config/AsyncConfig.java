package kr.polymarket.global.config;

import kr.polymarket.global.util.slack.SlackLoggingUtil;
import kr.polymarket.global.util.slack.model.SlackLoggingTargetType;
import kr.polymarket.global.util.slack.model.SlackLoggingType;
import kr.polymarket.global.util.slack.model.SlackWebhookCustomField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@EnableAsync
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AsyncConfig extends AsyncConfigurerSupport {

    private final SlackLoggingUtil slackLoggingUtil;

    /**
     * async 작업시 사용할 스레드 풀 bean
     * - 스레드는 상시 60개를 유지
     * - 스레드 대기큐 제한 x (비동기 작업 요청을 exception 없이 가용할때까지 받음)
     * @return Executor
     */
    @Bean
    public AsyncListenableTaskExecutor threadPoolTaskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(60);
        executor.setMaxPoolSize(60);
        executor.setThreadNamePrefix("async-"); // 스레드 풀 내부의 스레드 이름 접두사(prefix) 설정
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) -> {
            try {
                List<SlackWebhookCustomField> slackWebhookCustomFieldsList = List.of(
                        SlackWebhookCustomField.builder()
                                .label("Async Exception")
                                .text("exception in thread pool")
                                .build(),
                        SlackWebhookCustomField.builder()
                                .label("invoked class")
                                .text(method.getDeclaringClass().getName())
                                .build(),
                        SlackWebhookCustomField.builder()
                                .label("invoked method")
                                .text(method.getName())
                                .build(),
                        SlackWebhookCustomField.builder()
                                .label("Error Log")
                                .text(slackLoggingUtil.extractErrorMessage(e))
                                .build()
                );

                threadPoolTaskExecutor().execute(() -> {
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
        };
    }
}
