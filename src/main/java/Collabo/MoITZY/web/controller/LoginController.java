package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.domain.Member;
import Collabo.MoITZY.web.service.LoginService;
import Collabo.MoITZY.web.validation.form.MemberLoginForm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final HttpServletResponse httpServletResponse;

    @PostMapping("/mo-itzy/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody MemberLoginForm form, BindingResult bindingResult) {

        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) { // 입력값 검증
            log.info("Validation errors {} ", bindingResult.getAllErrors());
            response.put("status", "failure");
            response.put("message", "Validation errors");
            return ResponseEntity.badRequest().body(response);
        }

        Member loginMember = loginService.login(form);

        // 로그인 실패
        if (loginMember == null) {
            log.info("Login failed");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            response.put("status", "failure");
            response.put("message", "아이디 또는 비밀번호가 맞지 않습니다.");
            return ResponseEntity.status(401).body(response);
        }

        // 로그인 성공
        log.info("Login successful");

        // 세션 ID 생성------------------------------------------------------------------------
        // 쿠키 설정, 쿠키 유지기간 설정 없이 브라우저 세션 종료시 파기(세션 쿠키)
        Cookie cookie = new Cookie("test_Session", String.valueOf(loginMember.getId()));
        cookie.setHttpOnly(true); // 프론트 접근 불가 설정
        httpServletResponse.addCookie(cookie); // 쿠키를 응답에 추가
        //------------------------------------------------------------------------------------------

        response.put("status", "success");
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

}
