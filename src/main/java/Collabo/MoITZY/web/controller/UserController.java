package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.UserService;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/mo-itzy/join")
    public ResponseDto<?> join(@RequestBody UserJoinForm form) {
        return userService.join(form);
    }

    // 마이 페이지 요청
    @GetMapping("/mo-itzy/mypage")
    public ResponseDto<?> myPage(@RequestHeader("Authorization") String token) {
        return userService.findMember(token);
    }
}