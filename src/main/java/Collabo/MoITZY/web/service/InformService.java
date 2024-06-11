package Collabo.MoITZY.web.service;

import Collabo.MoITZY.dto.InformDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class InformService {

    private final InformRepository informRepository;

    // 공지사항 조회
    public ResponseDto<Page<InformDto>> getAllInforms(Pageable pageable) {
        Page<InformDto> informDtos = informRepository.getInformList(pageable);
        return ResponseDto.ok(OK, "공지사항 조회 성공", informDtos);
    }
}