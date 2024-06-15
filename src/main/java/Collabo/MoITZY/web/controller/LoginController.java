package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.LoginService;
import Collabo.MoITZY.web.validation.form.UserLoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 로그인
    @PostMapping("/mo-itzy/login")
    public ResponseDto<?> login(@RequestBody UserLoginForm form) {
        return loginService.login(form);
    }

    // 로그아웃
    @PostMapping("/mo-itzy/logout")
    public ResponseDto<?> logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }
}