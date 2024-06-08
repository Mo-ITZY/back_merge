package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.MemberService;
import Collabo.MoITZY.web.validation.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/mo-itzy/join")
    public ResponseDto<?> join(@RequestBody MemberJoinForm form) {
        return memberService.join(form);
    }
}