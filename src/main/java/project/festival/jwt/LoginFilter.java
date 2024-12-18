package project.festival.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.festival.domain.Member;
import project.festival.dto.CustomUserDetails;
import project.festival.repository.MemberRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

//@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/member/login");
    }

    private final AuthenticationManager authenticationManager;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

//    // /login 경로가 아닌 다른 경로에서 LoginFilter를 실행하고 싶을때 setFilterProcessesUrl로 설정 가능
//    @PostConstruct
//    public void init() {
//        setFilterProcessesUrl("/member/login");
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("LoginFilter attemptAuthentication에 들어옴");
        try {

//            // 요청 본문 로그로 확인
//            StringBuilder bodyContent = new StringBuilder();
//            try (BufferedReader reader = request.getReader()) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    bodyContent.append(line);
//                }
//            }
//            log.info("Request body content: {}", bodyContent.toString());

            //클라이언트 요청에서 username, password 추출
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(request.getReader(), Map.class);
            String loginId = loginData.get("loginId");
            String password = loginData.get("password");

//            log.info("loginId:{}", loginId);
//            log.info("password:{}", password);
            //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);

//            log.info("authToken{}",authToken);

            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            log.error("Error reading login data from request", e);
            throw new AuthenticationException("Invalid login request format", e) {};
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        log.info("LoginFilter에 로그인 성공");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String loginId = customUserDetails.getUsername();

        String token = jwtUtil.createJwt(loginId, 60 * 60 * 10L);

        response.addHeader("Authorization", "Bearer " + token);

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("로그인 실패시");
        response.setStatus(401);
    }
}
