package kr.polymarket.global.util.slack.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.List;

/**
 * 슬랙 웹훅 Block 요청 객체
 * 참고: https://api.slack.com/reference/block-kit/blocks
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SlackWebhookSectionBlockRequest {

    private BlockType type;

    private BlockElementField text;

    private List<BlockElementField> fields;

    public enum BlockType {
        section // section type
    }

    /**
     * 슬랙 웹훅 Section Filed 객체
     * 참고: https://api.slack.com/reference/block-kit/blocks#section
     */
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class BlockElementField {

        private BlockElementType type;

        private String text;

        public enum BlockElementType {
            mrkdwn
        }
    }
}
