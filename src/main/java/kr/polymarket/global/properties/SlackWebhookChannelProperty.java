package kr.polymarket.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("slack.webhook-channel")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class SlackWebhookChannelProperty {

    private final String loggingApiChannel;
    private final String loggingFrontChannel;
    private final String loggingMobileChannel;
}
