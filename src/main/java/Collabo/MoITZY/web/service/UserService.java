package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;

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

    // 중복 회원 검증
    private void validateDuplicatedMember(User user) {
        Optional<Member> findMember = memberRepository.findByLoginId(user.getName());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Optional<Member> findMember(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}