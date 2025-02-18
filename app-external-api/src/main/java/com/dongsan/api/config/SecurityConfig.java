package com.dongsan.api.config;

<<<<<<< HEAD:app-external-api/src/main/java/com/dongsan/api/config/SecurityConfig.java
import com.dongsan.api.domains.auth.security.filter.AuthFilter;
import com.dongsan.api.domains.auth.security.handler.CustomAccessDeniedHandler;
import com.dongsan.api.domains.auth.security.handler.CustomAuthenticationEntryPoint;
import com.dongsan.api.domains.auth.security.oauth2.handler.CustomSuccessHandler;
import com.dongsan.api.domains.auth.security.oauth2.service.CustomOAuthUserService;
import com.dongsan.api.domains.auth.service.JwtService;
=======
import com.dongsan.domains.auth.AuthService;
import com.dongsan.domains.auth.security.filter.AuthFilter;
import com.dongsan.domains.auth.security.handler.CustomAccessDeniedHandler;
import com.dongsan.domains.auth.security.handler.CustomAuthenticationEntryPoint;
import com.dongsan.domains.auth.security.oauth2.handler.CustomSuccessHandler;
import com.dongsan.domains.auth.security.oauth2.service.CustomOAuthUserService;
import com.dongsan.domains.auth.service.CookieService;
import com.dongsan.domains.auth.service.JwtService;
>>>>>>> 920be9371ff304630f249d16536e70a3e734d4d6:app-external-api/src/main/java/com/dongsan/config/SecurityConfig.java
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuthUserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * 정적 파일은 필터 안 타게 설정
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/public/**"),
                        new AntPathRequestMatcher("/favicon.ico"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/v3/api-docs/**")
                );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:8080",
                        "http://localhost:3000",
                        "http://dongsanwalk.site",
                        "http://api.dongsanwalk.site:8080",
                        "https://dongsanwalk.site",
                        "https://www.dongsanwalk.site",
                        "https://api.dongsanwalk.site",
                        "http://front.dongsanwalk.site:3000"
                )
        );
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain oAuth2SecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**"
                        )
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http, JwtService jwtService, CookieService cookieService, AuthService authService,
                                                      CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
            throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/**"
                        )
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                /**
                 * JwtAuthFilter 를 @Component 로 등록하면 WebSecurityCustomizer 에서 ignoring 을 해준 경로에도 jwt 필터가 실행된다.
                 * 관련 링크: https://stackoverflow.com/questions/39152803/spring-websecurity-ignoring-doesnt-ignore-custom-filter/40969780#40969780
                 * shouldNotFilter 로 jwt 필터에서 해당 경로를 타지 않도록 해주거나
                 * 아래처럼 컴포넌트로 등록하지 않고, 수동으로 등록하는 방법을 사용할 수 있다.
                 */
                .addFilterBefore(new AuthFilter(jwtService, cookieService, authService),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Admin 경로에 있어야 하는 role
                        //.requestMatchers("/admin/**").hasRole("ADMIN")
                        // 인증 없이 접근 가능
                        .requestMatchers(
                                "/",
                                "/health",
                                "/dev/**",
                                "/auth/refresh"
                        ).permitAll()
                        // 이외 요청 모두 jwt 필터를 타도록 설정
                        .anyRequest()
                        .authenticated())
                        .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                /**
                                 *  인증되지 않은 요청일 경우
                                 *
                                 *  SecurityContext 에 등록되지 않았을 때 호출된다.
                                 */
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                /**
                                 * 인증은 되었으나, 해당 요청에 대한 권한이 없는 사용자인 경우
                                 *
                                 * .hasRole 로 권한을 검사할 때 권한이 부족하여 요청이 거부되었을 때 호출된다.
                                 */
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}
