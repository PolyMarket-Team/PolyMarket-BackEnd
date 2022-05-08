package kr.polymarket.global.util.slack.model;

import lombok.*;

/**
 * 로깅 메시지에 사용할 커스텀 데이터
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class SlackWebhookCustomField {
    String label;
    String text;
}
