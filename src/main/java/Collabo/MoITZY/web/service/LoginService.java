package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Member;
import Collabo.MoITZY.dto.LoginDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.MemberRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public ResponseDto<LoginDto> login(MemberLoginForm form) {
        return getLoginDtoResponseDto(form);
    }

    private ResponseDto<LoginDto> getLoginDtoResponseDto(MemberLoginForm form) {
        String loginId = form.getLoginId();
        String password = form.getPassword();

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

        int exprTIme = 3600;

        String token = tokenProvider.createToken(loginId, exprTIme);

        LoginDto loginDto = new LoginDto(token, exprTIme, findMember);

        return ResponseDto.ok(OK, "로그인 성공", loginDto);
    }

//    /**
//     * return null 이면 로그인 실패
//     * 로그인할 때 기입한 password와 db에 저장된 password가 같은지 확인하는 메소드
//     */
//    public Member login(String loginId, String password) {
//        // 중복된 멤버 검색 시 예외 처리 및 null 반환, 비밀번호 불일치 시 null 반환
//        Member findMember = memberRepository.findByLoginId(loginId)
//                .filter(m -> m.getPassword().equals(password))
//                .orElse(null);
//
//        log.info("findMember={} ", loginId);
//
//        if (findMember == null) {
//            log.info("Login failed");
//            return null;
//        }
//
//        log.info("Login successful");
//        return findMember;
//    }
}