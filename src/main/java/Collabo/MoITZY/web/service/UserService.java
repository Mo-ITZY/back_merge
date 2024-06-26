package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.MyPageDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.exception.MemberNotFoundException;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.PasswordCheckForm;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import Collabo.MoITZY.web.validation.form.UserUpdateForm;
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

    // 회원 정보 조회
    public ResponseDto<MyPageDto> findMember(String token) {
        try {
            Member member = tokenProvider.getValidateMember(token);
            String role = member.getRole();
            if (role.equals("USER")) {
                User user = (User) member;
                return ResponseDto.ok(OK, "회원 정보 조회 성공", new MyPageDto(user.getName(), user.getImg(), user.getRoiList().size()));
            } else if (role.equals("ADMIN")) {
                return ResponseDto.ok(OK, "회원 정보 조회 성공", new MyPageDto(null, null, 0));
            } else {
                return ResponseDto.error(BAD_REQUEST, "로그인 후 이용해주세요");
            }

        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        }
    }

    // 회원 정보 수정
    @Transactional
    public ResponseDto<Void> updateMember(String token, UserUpdateForm form) {
        try {
            User user = tokenProvider.getValidateUser(token);
            user.updateUser(form);
            memberRepository.save(user);

            return ResponseDto.ok(OK, "회원 정보 수정 성공");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        }
    }

    // 회원 탈퇴
    @Transactional
    public ResponseDto<?> deleteMember(String token) {
        try {
            User user = tokenProvider.getValidateUser(token);

            log.info("user : loginId {}, password{}", user.getLoginId(), user.getPassword());
            memberRepository.existsByLoginIdAndPassword(user.getLoginId(), user.getPassword());
            memberRepository.delete(user);

            return ResponseDto.ok(OK, "회원 탈퇴 성공");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        }
    }

    // 중복 회원 검증
    private void validateDuplicatedMember(User user) {
        Optional<Member> findMember = memberRepository.findByLoginId(user.getName());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 비밀번호 확인
    public ResponseDto<?> isPasswordCorrect(String token, PasswordCheckForm form) {
        try {
            User user = tokenProvider.getValidateUser(token);

            log.info("user: {}, password: {}", user.getLoginId(), form.getPassword());
            if (memberRepository.existsByLoginIdAndPassword(user.getLoginId(), form.getPassword())) {
                return ResponseDto.ok(OK, "비밀번호 일치");
            }
            return ResponseDto.error(BAD_REQUEST, "비밀번호 불일치");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        }
    }
}