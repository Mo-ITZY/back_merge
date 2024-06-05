package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.web.service.MemberService;
import Collabo.MoITZY.web.validation.form.MemberJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입git
    @PostMapping("/mo-itzy/join")
    public String join(@RequestBody MemberJoinForm form) {
        memberService.join(form);
        return "회원 가입 완료";
    }
}