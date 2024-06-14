package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.domain.member.Admin;
import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.dto.InformDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.exception.MemberNotFoundException;
import Collabo.MoITZY.web.repository.InformRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.InformForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class InformService {

    private final InformRepository informRepository;
    private final TokenProvider tokenProvider;

    // 공지사항 조회
    public ResponseDto<Page<InformDto>> getAllInforms(Pageable pageable) {
        Page<InformDto> informDtos = informRepository.getInformList(pageable);
        return ResponseDto.ok(OK, "공지사항 조회 성공", informDtos);
    }

    // 공지사항 작성
    @Transactional
    public ResponseDto<Void> writeInform(String token, InformForm form) {
        try {
            tokenProvider.getValidateAdmin(token);

            informRepository.save(new Inform(form.getTitle(), form.getContent(), LocalDateTime.now()));
            return ResponseDto.ok(OK, "공지사항 작성 성공");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        }
    }
}