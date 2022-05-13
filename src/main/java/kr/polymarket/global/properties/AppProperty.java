package kr.polymarket.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("app")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class AppProperty {
    /**
     * 어플리케이션 환경
     * test, local(로컬), dec(개발서버), prod(실서버)
     */
    private final String env;
}
