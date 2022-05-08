package kr.polymarket.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

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
}
