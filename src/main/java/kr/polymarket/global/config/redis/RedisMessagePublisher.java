package kr.polymarket.global.config.redis;

import kr.polymarket.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    //redis 발행 서비스, 메시지 발행시 redis 구독 서비스가 메시지를 처리함
    public void publish(ChatMessageDto chatMessage) {
        redisTemplate.convertAndSend(String.valueOf(chatMessage.getChatRoomId()), chatMessage);
    }

}
