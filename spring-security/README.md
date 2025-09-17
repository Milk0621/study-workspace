# Spring Security 학습 정리

## Spring Security란?
- Spring 기반 인증(Authentication)과 권한(Authorization)을 처리하는 보안 프레임워크
- Filter 기반 구조로 DispatcherServlet 앞단에서 요청을 가로채어 보안 검사
- 기본 세션/쿠키 방식 지원 + JWT, OAuth2 등 확장 가능

## 기본 동작 원리
1. 사용자 로그인 요청
2. UsernamePasswordAuthenticationFilter가 요청을 가로챔
3. AuthenticationManager에서 적절한 Provider에게 넘김
4. UserDetailsService에서 사용자 정보 조회
5. PasswordEncoder(예: BCryptPasswordEncoder)로 비밀번호 검증
6. 성공 시 SecurityContext에 저장, 핸들러 실행

## Filter 구조
- DelegatingFilterProxy -> FilterChainProxy -> SecurityFilterChain -> 다수의 Security Filter 실행
- 요청 URL 패턴별로 적절한 SecurityFilterChain이 선택됨
