package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {
	
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.addAllowedOriginPattern("*");	// 모든 Origin(도메인) 허용 - 배포 시에는 구체적으로 제한
		cfg.addAllowedHeader("*");			// 모든 헤더 허용
		cfg.addAllowedMethod("*");			// 모든 HTTP 메서드 허용
		cfg.setAllowCredentials(true);		// 자격 증명(쿠키/Authorization 헤더) 포함 요청 허용
		
		// URL 패턴마다 위의 CORS 설정을 적용하는 소스 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);	// 모든 경로(/**)에 대해 방금 만든 CORS 정책(cfg) 등록
		return new CorsFilter(source);					// 최종 CORS 필터 생성 후 반환
	}
}
