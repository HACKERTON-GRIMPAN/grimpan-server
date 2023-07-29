package com.grimpan.emodiary.configuration;


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
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/kakao", "/auth/kakao/callback",
                        "/auth/google", "/auth/google/callback",
                        "/auth/apple", "/auth/apple/callback",
                        "/auth/refresh", "/image").permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
