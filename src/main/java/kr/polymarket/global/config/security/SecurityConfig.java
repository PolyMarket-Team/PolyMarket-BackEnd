package kr.polymarket.global.config.security;

import kr.polymarket.global.config.security.jwt.CustomAccessDeniedHandler;
import kr.polymarket.global.config.security.jwt.CustomAuthenticationEntryPoint;
import kr.polymarket.global.config.security.jwt.JwtAuthenticationFilter;
import kr.polymarket.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/swagger-ui/**"
                        , "/swagger-resources/**"
                        , "/v2/api-docs"
                        , "/favicon.ico"
                        , "/configuration/ui"
                        , "/configuration/security"
                        , "/error"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //기본설정 미사용
                .csrf().disable() //csrf 보안 미사용
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt로 인증하므로 세션미사용

                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").access("hasIpAddress('127.0.0.1') or hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('192.168.0.10')")
                .antMatchers(HttpMethod.GET, "/users/**").permitAll()
                .antMatchers(HttpMethod.POST,"/users/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}