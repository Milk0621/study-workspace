package com.example.security.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/public/hello")
	public String publicHello() {
		return "누구나 접근 가능";
	}
	
	@GetMapping("/user/hello")
	public String userHello(@AuthenticationPrincipal UserDetails principal) {
		// @AuthenticationPrincipal : 지금 요청의 로그인 사용자(principal)를 컨트롤러 파라미터로 바로 받아 쓰게 해줌
		
		// 현재 요청의 보안 컨텍스트(SecurityContext)에서 Authentication 객체를 꺼내고 -> 그 안의 getPrincipal() 값을 알맞은 타입으로 주입
		// 즉, 내부적으로는 SecurityContextHolder.getContext().getAuthentication().getPrincipal()을 꺼내 자동으로 넣어주는 어노테이션
		return "USER 접근 가능 - " + principal.getUsername();
	}
	
	@GetMapping("/admin/hello")
	public String adminHello(@AuthenticationPrincipal UserDetails principal) {
		return "ADMIN 접근 가능 - " + principal.getUsername();
	}
	
	@GetMapping("/me")
	public String me(@AuthenticationPrincipal UserDetails principal) {
	    return principal == null ? "anonymous" : ("me: " + principal.getUsername());
	}
}
