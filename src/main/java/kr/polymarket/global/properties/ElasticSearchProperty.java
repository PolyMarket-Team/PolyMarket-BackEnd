package kr.polymarket.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("elasticsearch")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class ElasticSearchProperty {

    private final String[] hosts;
}
