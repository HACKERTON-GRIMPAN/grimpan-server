package com.grimpan.emodiary.configuration;


import com.grimpan.emodiary.common.Constants;
import com.grimpan.emodiary.security.CustomUserDetailService;
import com.grimpan.emodiary.security.JwtAccessDeniedHandler;
import com.grimpan.emodiary.security.JwtEntryPoint;
import com.grimpan.emodiary.security.filter.JwtAuthenticationFilter;
import com.grimpan.emodiary.security.filter.JwtExceptionFilter;
import com.grimpan.emodiary.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetailService;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 개발용이므로 후에는 삭제 예정(모든 요청 허용 상태 - 위험)
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry.anyRequest().permitAll())

                // .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                //         .requestMatchers(Constants.NO_NEED_AUTH_URLS).permitAll()
                //         .anyRequest().authenticated())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)

                .getOrBuild();
    }
}
