package Collabo.MoITZY.web.service;

import Collabo.MoITZY.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class LoginServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void loginTest() {
        String loginId = "test";
        String password = "1234";

        boolean existByLoginId = memberRepository.existsByLoginId(loginId);

        boolean existedByPassword = memberRepository.existsByLoginIdAndPassword(loginId, password);

        log.info("아이디 검증 : {}", existByLoginId);
        log.info("비밀번호 검증: {}", existedByPassword);
    }
}