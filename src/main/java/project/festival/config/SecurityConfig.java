package project.festival.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import project.festival.jwt.JWTFilter;
import project.festival.jwt.JWTUtil;
import project.festival.jwt.LoginFilter;
import project.festival.repository.MemberRepository;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity //스프링 시큐리티를 활성화하고 웹 보안 설정을 구성
@RequiredArgsConstructor
public class SecurityConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     *
     *  .requestMathcers를 통해 특정 url에 들어오는 요청들에 대해서 인가작업을 수행할 수 있다.
     * .permitAll()을 사용하면 모두 사용 가능
     * .anyRequest().authenticated()은  그 외 요청들은 모두 인증이 되어야 한다는 뜻
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        log.info("securityFilterChain");

        http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3030"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }
        })));

        http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/member/signup","/member/login","/").permitAll()
                        .anyRequest().authenticated());// 모든 요청은 인증 필요

        http.httpBasic((httpBasic) -> httpBasic.disable());

        http.formLogin(form -> form.disable());

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        //AuthenticationManager()와 JWTUtil 인수 전달
//        log.info("LoginFilter 등록");
        http.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //JWTFilter 등록
        http.addFilterAt(new JWTFilter(jwtUtil), LoginFilter.class);



        // spring security는 기본적으로 인증이 필요한 요청에 대해 세션을 생성하고 관리하는 기능을 제공함
        // JWT 토큰 인증 방식에서는 세션을 생성할 필요가 없기 때문에 STATELESS으로 설정
        // JWT 사용시, 인증 정보를 클라이언트가 직접 가지고 있음 -> 서버가 상태를 관리할 필요가 없음
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
