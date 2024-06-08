package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.dto.InformDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InformRepositoryCustom {
    Page<InformDto> getInformList(Pageable pageable);
}