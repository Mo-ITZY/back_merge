package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.dto.InformDto;
import Collabo.MoITZY.web.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformService {

    private final InformRepository informRepository;

    // 공지사항 전체 조회
    public List<InformDto> getAllInforms() {
        return informRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 공지사항 단건 조회
    private InformDto convertToDto(Inform inform) {
        return new InformDto(inform.getId(), inform.getTitle(), inform.getContent(), inform.getWriteDate());
    }
}