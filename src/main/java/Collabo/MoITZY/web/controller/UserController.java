package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.UserService;
import Collabo.MoITZY.web.validation.form.PasswordCheckForm;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import Collabo.MoITZY.web.validation.form.UserUpdateForm;
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

    // 회원 탈퇴
    @PostMapping("/mo-itzy/mypage/delete")
    public ResponseDto<?> delete(@RequestHeader("Authorization") String token) {
        return userService.deleteMember(token);
    }

    // 회원 정보 수정
    @PostMapping("/mo-itzy/mypage/update")
    public ResponseDto<?> update(@RequestHeader("Authorization") String token, @RequestBody UserUpdateForm form) {
        return userService.updateMember(token, form);
    }

    // 마이 페이지 요청
    @GetMapping("/mo-itzy/mypage")
    public ResponseDto<?> myPage(@RequestHeader("Authorization") String token) {
        return userService.findMember(token);
    }

    // 비밀번호 확인
    @PostMapping("/mo-itzy/password-check")
    public ResponseDto<?> checkPassword(@RequestHeader("Authorization") String token, @RequestBody PasswordCheckForm form) {
        return userService.isPasswordCorrect(token, form);
    }
}