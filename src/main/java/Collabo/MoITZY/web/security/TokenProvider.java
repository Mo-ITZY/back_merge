package Collabo.MoITZY.web.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;

@Slf4j
@Service
public class TokenProvider {

    private static final String SECURITY_KEY = "jwt-token-of-mo-itzy-application"; // 비밀키

    // JWT 생성 메서드
    public String createJwt(String loginId, int duration) {
        try {
            // 만료 시간 : 현재 시간 기준 주어진 시간 뒤
            Instant now = Instant.now();
            Instant exprTime = now.plusSeconds(duration);

            // JWT Claim 생성
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(loginId)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(exprTime))
                    .build();

            // JWT 서명
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256), claimsSet
            );

            // HMAC 서명을 사용하여 JWT 서명
            JWSSigner signer = new MACSigner(SECURITY_KEY.getBytes());

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("토큰 생성에 실패했습니다: " + e.getMessage(), e);
        }
    }

    // JWT 검증 메서드
    public String validateJwt(String token) {
        try {
            // 서명 확인을 통한 JWT 검증
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECURITY_KEY.getBytes());
            if (signedJWT.verify(verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                // 서명이 유효하지 않은 경우
                log.info("서명이 유효하지 않은 토큰입니다.");
                return null;
            }
        } catch (Exception e) {
            log.info("토큰 검증에 실패했습니다: " + e.getMessage());
            return null;
        }
    }
}