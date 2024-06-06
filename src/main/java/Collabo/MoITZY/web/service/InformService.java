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

    public List<InformDto> getAllInforms() {
        return informRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InformDto convertToDto(Inform inform) {
        return new InformDto(inform.getId(), inform.getTitle(), inform.getContent(), inform.getWriteDate());
    }
}