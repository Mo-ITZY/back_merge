package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.domain.Member;
import Collabo.MoITZY.web.service.LoginService;
import Collabo.MoITZY.web.validation.form.MemberLoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import static Collabo.MoITZY.web.session.SessionConst.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/mo-itzy/login")
    public ResponseEntity<Map<String, String>> login(
            @Validated @RequestBody MemberLoginForm form,
            BindingResult bindingResult, HttpServletRequest request) {

        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) { // 입력값 검증
            log.info("Validation errors {} ", bindingResult.getAllErrors());
            response.put("status", "failure");
            response.put("message", "Validation errors");
            return ResponseEntity.badRequest().body(response);
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

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
        response.put("status", "success");
        response.put("message", "Login successful");

        // 세션이 있으면 세션 반환, 없으면 생성
        HttpSession session = request.getSession();

        // 세션에 로그인 회원 정보 보관
        session.setAttribute(LOGIN_MEMBER, loginMember);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/mo-itzy/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.put("status", "failure");
            response.put("message", "세션이 존재하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 세션 만료
        session.invalidate();

        response.put("status", "success");
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }

}
