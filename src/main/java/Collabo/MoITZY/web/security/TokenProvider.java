package Collabo.MoITZY.web.security;

import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.exception.MemberNotFoundException;
import Collabo.MoITZY.web.repository.MemberRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberRepository memberRepository;

    private static final String SECURITY_KEY = "jwt-token-of-mo-itzy-application"; // 비밀키

    // JWT 생성 메서드
    public String createJwt(String loginId, int duration) {
        try {
            // 만료 시간 : 현재 시간 + 주어진
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

    // 검증된 유저 얻기
    public User getValidateUser(String token) {
        Member member = getMemberByToken(token);
        getValidateRole(member, "USER");
        return (User) member;
    }

    // 검증된 관리자 얻기
    public void getValidateAdmin(String token) {
        Member member = getMemberByToken(token);
        getValidateRole(member, "ADMIN");
    }

    // 검증된 멤버 얻기
    public Member getValidateMember(String token) {
        return getMemberByToken(token);
    }

    // 토큰을 통해 멤버 얻기
    private Member getMemberByToken(String token) {
        log.info("token: {}", token);

        String loginId = validateJwt(token);
        log.info("loginId: {}", loginId);

        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    // 역할 검증 메서드
    private void getValidateRole(Member member, String requiredRole) {
        String role = member.getRole();
        log.info("role: {}", role);
        if (!role.equals(requiredRole)) {
            throwRoleException(role, requiredRole);
        }
    }

    // 역할 예외 처리 메서드
    private void throwRoleException(String role, String requiredRole) {
        if (requiredRole.equals("USER") && role.equals("ADMIN")) {
            throw new MemberNotFoundException("관리자는 접근할 수 없습니다.");
        } else if (requiredRole.equals("ADMIN") && role.equals("USER")) {
            throw new MemberNotFoundException("사용자는 접근할 수 없습니다.");
        } else {
            throw new MemberNotFoundException("로그인 이후 이용해주세요.");
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