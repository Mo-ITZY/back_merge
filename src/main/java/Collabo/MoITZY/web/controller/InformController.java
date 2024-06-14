package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.InformService;
import Collabo.MoITZY.web.validation.form.InformForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InformController {

    private final InformService informService;

    // 공지사항 조회 - 회원 정보 뿌리기
    @GetMapping("/mo-itzy/notice")
    public ResponseDto<?> getInforms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return informService.getAllInforms(pageable);
    }

    // 공지사항 작성
    @PostMapping("/mo-itzy/notice")
    public ResponseDto<?> writeInform(
            @RequestHeader("Authorization") String token,
            @RequestBody InformForm form) {
        return informService.writeInform(token, form);
    }
}

