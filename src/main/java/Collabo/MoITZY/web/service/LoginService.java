package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Member;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.validation.form.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * return null 이면 로그인 실패
     * 로그인할 때 기입한 password와 db에 저장된 password가 같은지 확인하는 메소드
     */
    public Member login(MemberLoginForm form) {
        log.info("attempting loginId={}", form.getLoginId());

        // 중복된 멤버 검색 시 예외 처리 및 null 반환, 비밀번호 불일치 시 null 반환
        Member findMember = memberRepository.findByLoginId(form.getLoginId())
                .filter(m -> m.getPassword().equals(form.getPassword()))
                .orElse(null);

        log.info("findMember={} ", form.getLoginId());

        if (findMember == null) {
            log.info("Login failed");
            return null;
        }

        log.info("Login successful");
        return findMember;
    }
}