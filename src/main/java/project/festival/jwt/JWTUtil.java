package project.festival.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWTUtil {

    private SecretKey secretKey;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        //applicaton.properties에서 가져온 secret키를 암호화하여 객체 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    //검증을 진행하는 메소드들
    public String getLoginId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId", String.class);
    }

    public Boolean iExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //로그인 성공시 토큰을 생성하는 부분
    public String createJwt(String loginId,Long expiredMs) {
//        log.info("expiredMS:{}",expiredMs);
//        log.info("current time:{}", System.currentTimeMillis());
        return Jwts.builder()
                .claim("loginId", loginId)
                .issuedAt(new Date(System.currentTimeMillis())) //현재 발행시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) //토큰의 소멸 시간(현재 발행시간 + 토큰이 유효한 시간)
                .signWith(secretKey)
                .compact();
    }
}
