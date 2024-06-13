package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.MyPageDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 회원 가입
    @Transactional
    public ResponseDto<Void> join(UserJoinForm userJoinForm) {
        User user = User.createUser(userJoinForm);
        try {
            validateDuplicatedMember(user);
        } catch (IllegalStateException e) {
            return ResponseDto.error(CONFLICT, e.getMessage());
        }
        memberRepository.save(user);
        return ResponseDto.ok(OK, "회원 가입 성공");
    }

    // 회원 단건 조회
    public ResponseDto<MyPageDto> findMember(String token) {
        log.info("token: {}", token);
        String loginId = tokenProvider.validateJwt(token);
        log.info("loginId: {}", loginId);
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);

        if (findMember.isEmpty()) {
            return ResponseDto.error(NOT_FOUND, "회원 정보를 찾을 수 없습니다.");
        }

        Member member = findMember.get();
        String role = member.getRole(member);
        log.info("role: {}", role);

        if (role.equals("USER")) {
            User user = (User) member;

            return ResponseDto.ok(OK, "회원 정보 조회 성공", new MyPageDto(user.getName(), user.getImg(), user.getRoiList().size()));
        } else if (role.equals("ADMIN")) {
            return ResponseDto.error(FORBIDDEN, "관리자는 접근할 수 없습니다.");
        } else {
            return ResponseDto.error(FORBIDDEN, "로그인 이후 이용해주세요.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 중복 회원 검증
    private void validateDuplicatedMember(User user) {
        Optional<Member> findMember = memberRepository.findByLoginId(user.getName());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}