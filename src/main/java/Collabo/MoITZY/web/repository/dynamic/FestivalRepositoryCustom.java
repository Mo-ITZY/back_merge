package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.web.repository.cond.FestivalSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FestivalRepositoryCustom {

    Page<FestivalDto> searchFestival(FestivalSearchCond cond, Pageable pageable);

}