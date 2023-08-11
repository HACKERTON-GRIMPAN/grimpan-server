package com.grimpan.emodiary.configuration;

import com.grimpan.emodiary.common.Constants;
import com.grimpan.emodiary.common.UserIdInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMCVConfig implements WebMvcConfigurer {
    // Resolver 작성 예정

    // 인증 후 User Id request에 넣어주기 위한 Interceptor 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserIdInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(Constants.NO_NEED_AUTH_URLS);
    }
}
