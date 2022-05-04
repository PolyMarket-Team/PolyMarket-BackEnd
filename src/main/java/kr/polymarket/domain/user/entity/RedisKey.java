package kr.polymarket.domain.user.entity;

import lombok.Getter;

@Getter
public enum RedisKey {
    CODE(""), REFRESH("REFRESH");

    private String key;

    RedisKey(String key) {
        this.key = key;
    }
}
