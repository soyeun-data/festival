package project.festival.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.festival.domain.LoginEntity;
import project.festival.dto.CustomUserDetails;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWTFilter에 들어옴");
        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer")) {
            log.info("token null");
            filterChain.doFilter(request, response);

            return; //Authorization 헤더가 없거나 토큰이 없을 경우 메소드 종료
        }

        log.info("authorization 검증 시작");

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        log.info("token:{}", token);

        //토큰 소멸 시간 검증
        if (jwtUtil.iExpired(token)) {
            log.info("token expired");
            filterChain.doFilter(request, response);
            return; //토큰이 소멸되었으면 메소드 종료
        }

        //토큰에서 loginId 획득
        String loginId = jwtUtil.getLoginId(token);

        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setLoginId(loginId);
        loginEntity.setPassword("temppassword");

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(loginEntity);

        //스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);


    }
}
