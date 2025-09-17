package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				// CSRF(Cross Site Request Forgery, 크로스 사이트 요청 위조)
				// 웹 보안 취약점 중 하나로, 인증된 사용자가 자신의 의지와는 무관하게 웹 애플리케이션에 공격자가 의도한 특정 요청을 보내도록 유도하는 것
				// 학습/테스트 또는 JWT 기반(Stateless) API에서 보통 꺼두지만, 폼 로그인 + 세션 기반에선 기본적으로 켜두는 게 권장
				.csrf(csrf -> csrf.disable())
				
				// 인가(Authorization) 규칙 정의: 어떤 URL에 누가 접근할 수 있는지 
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/public/**").permitAll()	// GET 메서드에 한해 모두 허용
						.requestMatchers("/user/**").hasRole("USER")				// USER
						.requestMatchers("/admin/**").hasRole("ADMIN")				// ADMIN
						.anyRequest().authenticated()								// 그 외 모든 요청은 "인증"만 되어 있으면 접근 가능
				)
				
				// 폼 로그인 사용(스프링이 제공하는 기본 로그인 페이지/필터 적용)
				.formLogin(form -> form
						// 로그인 성공 시 이동 경로
						// true: 이전에 접근 시도했던 자원이 있어도 무시하고 무조건 이 경로로
						.defaultSuccessUrl("/public/hello", true)					
						// 이 외에도 .loginPage("커스텀 경로") / .loginProcessingUrl("폼 처리 URL") 등 옵션 존재
				)
				.logout(Customizer.withDefaults())
				.build();
		
	}
	
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		
		// withUsername 빌더로 UserDetails 생성
		UserDetails user = User.withUsername("user")
				.password(encoder.encode("1234"))		// 평문 "1234"를 BCrypt로 해시
				.roles("USER")							// "ROLE_USER" 권한으로 저장
				.build();
	
		UserDetails admin = User.withUsername("admin")
				.password(encoder.encode("1234"))
				.roles("ADMIN")
				.build();
		
		// InMemoryUserDetailsManager
		// 메모리 안에 UserDetails 객체를 저장해두는 관리자
		// DB를 사용하는 것이 아닌, 애플리케이션 메모리에 계정을 들고 있는 방식 (휘발성)
		return new InMemoryUserDetailsManager(user, admin);
		// 두 계정을 메모리에 저장하는 UserDetailsService 구현체를 반환하고,
		// 로그인 시 스프링 시큐리티가 이 빈의 loadUserByUsername()을 호출해 사용자 정보를 찾음
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
