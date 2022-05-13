package kr.polymarket.domain.user.repository;

import lombok.Getter;

@Getter
public enum RedisKeyPrefix {
    EMAIL_AUTH_CODE("EMAIL_AUTH_CODE"), REFRESH("REFRESH");

    private final String keyPrefix;

    private static final String separateStr = ":";

    RedisKeyPrefix(String key) {
        this.keyPrefix = key;
    }

    public String buildKey(String key) {
        return keyPrefix + separateStr + key;
    }
}
