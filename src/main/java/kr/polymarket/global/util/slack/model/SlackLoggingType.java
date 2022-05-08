package kr.polymarket.global.util.slack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackLoggingType {
    INFO, WARN, DEBUG, ERROR
}
