package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.domain.ROI;
import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.web.repository.cond.FestivalSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FestivalRepositoryCustom {

    Page<FestivalDto> searchFestival(FestivalSearchCond cond, Pageable pageable);

    Page<FestivalDto> searchLikeFestival(List<ROI> roiList, Pageable pageable);

    FestivalDto findFestival(Long id);
}