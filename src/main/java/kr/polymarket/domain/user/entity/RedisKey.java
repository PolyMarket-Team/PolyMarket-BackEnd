package kr.polymarket.domain.user.entity;

import lombok.Getter;

@Getter
public enum RedisKey {
    EMAIL_AUTH_CODE("EMAIL_AUTH_CODE"), REFRESH("REFRESH");

    private String key;

    RedisKey(String key) {
        this.key = key;
    }
}
