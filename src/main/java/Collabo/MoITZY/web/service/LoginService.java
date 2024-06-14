package Collabo.MoITZY.web.service;

import Collabo.MoITZY.dto.LoginDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.UserLoginForm;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            validateLoginCredentials(form.getLoginId(), form.getPassword());

            String token = generateToken(form.getLoginId());
            LoginDto loginDto = new LoginDto(token, 3600);

            return ResponseDto.ok(OK, "로그인 성공", loginDto);
        } catch (EntityNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseDto.error(INTERNAL_SERVER_ERROR, "서버 오류");
        }
    }

    private void validateLoginCredentials(String loginId, String password) {
        if (!memberRepository.existsByLoginId(loginId)) {
            throw new EntityNotFoundException("아이디가 존재하지 않습니다.");
        }
        if (!memberRepository.existsByLoginIdAndPassword(loginId, password)) {
            throw new EntityNotFoundException("비밀번호가 일치하지 않습니다.");
        }
        if (memberRepository.findByLoginId(loginId).isEmpty()) {
            throw new EntityNotFoundException("고객 정보가 존재하지 않습니다.");
        }
    }

    private String generateToken(String loginId) {
        int exprTime = 3600;
        return tokenProvider.createJwt(loginId, exprTime);
    }

    public ResponseDto<?> logout(String token) {
        return ResponseDto.ok(OK, "로그아웃 성공");
    }
}