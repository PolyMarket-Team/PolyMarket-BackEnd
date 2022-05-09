package kr.polymarket.global.util.slack.model;

import kr.polymarket.global.properties.SlackWebhookChannelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
public enum SlackLoggingTargetType {
    API, FRONT, MOBILE;

    /**
     * slack logging webhook channel url
     */
    @Setter(AccessLevel.PROTECTED)
    private String webhookChannelUrl;

    @Component
    @RequiredArgsConstructor
    public static class SlackWebhookChannelPropertyInjector {

        private final SlackWebhookChannelProperty slackWebhookChannelProperty;

        @PostConstruct
        public void postConstruct() {
            API.setWebhookChannelUrl(slackWebhookChannelProperty.getLoggingApiChannel());
            FRONT.setWebhookChannelUrl(slackWebhookChannelProperty.getLoggingFrontChannel());
            MOBILE.setWebhookChannelUrl(slackWebhookChannelProperty.getLoggingMobileChannel());
        }
    }

}
