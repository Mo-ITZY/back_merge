package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Member;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.validation.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(MemberJoinForm memberJoinForm) {
        Member member = Member.createMember(memberJoinForm);
        validateDuplicatedMember(member); //중복 회원 검증
        return memberRepository.save(member).getId();
    }

    // 중복 회원 검증
    private void validateDuplicatedMember(Member member) {
        Optional<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
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