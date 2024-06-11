package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.member.Admin;
import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.LoginDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.UserLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 로그인
    public ResponseDto<LoginDto> login(UserLoginForm form) {
        String loginId = form.getLoginId();
        String password = form.getPassword();
        return getLoginDtoResponseDto(loginId, password);
    }

    private ResponseDto<LoginDto> getLoginDtoResponseDto(String loginId, String password) {
        try {
            boolean existByLoginId = memberRepository.existsByLoginId(loginId);
            if(!existByLoginId) {
                return ResponseDto.error(NOT_FOUND, "아이디가 존재하지 않습니다.");
            }
            boolean existedByPassword = memberRepository.existsByLoginIdAndPassword(loginId, password);
            if(!existedByPassword) {
                return ResponseDto.error(NOT_FOUND, "비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseDto.error(INTERNAL_SERVER_ERROR, "서버 오류");
        }

        boolean present = memberRepository.findByLoginId(loginId).isPresent();

        if (!present) {
            return ResponseDto.error(NOT_FOUND, "고객 정보가 존재하지 않습니다.");
        }

        Member findMember = memberRepository.findByLoginId(loginId).get();
        String memberType;

        if (findMember instanceof Admin) {
            memberType = "ADMIN";
        } else if (findMember instanceof User) {
            memberType = "USER";
        } else {
            memberType = "UNKNOWN";
        }

        int exprTIme = 3600;

        String token = tokenProvider.createToken(loginId, exprTIme);

        LoginDto loginDto = new LoginDto(token, exprTIme, memberType, findMember);

        return ResponseDto.ok(OK, "로그인 성공", loginDto);
    }
}