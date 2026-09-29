package com.likelion.NetflixClone.global.config;

import com.likelion.NetflixClone.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS & CSRF 설정 해제
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 2. 세션 미사용
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. 권한 허용
                .authorizeHttpRequests(auth -> auth
                        // 인증 및 Swagger 관련 경로
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/images/**" // 업로드된 정적 이미지 파일 접근 허용
                        ).permitAll()

                        // 콘텐츠 관련 경로
                        .requestMatchers(HttpMethod.GET, "/api/v1/contents/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/contents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/contents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/contents/**").hasRole("ADMIN")

                        // 4주차 신규 기능 경로 (찜하기 & 이미지 업로드)
                        .requestMatchers("/api/v1/wishlist/**").authenticated() // 찜하기는 로그인된 유저 누구나
                        .requestMatchers(HttpMethod.POST, "/api/v1/images/**").hasRole("ADMIN") // 이미지 업로드는 ADMIN

                        .anyRequest().authenticated()
                )

                // 4. JWT 필터 위치 지정
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}