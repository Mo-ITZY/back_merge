package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.dto.FestivalApiDto;
import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.web.repository.FestivalRepository;
import Collabo.MoITZY.web.repository.cond.FestivalSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;


    // api 데이터를 가져와서 저장
    public List<Festival> convertToFestivals(FestivalApiDto festivalApiDto) {
        return festivalApiDto.getData().stream()
                .map(Festival::ApiToFestival)
                .collect(toList());
    }

    // 축제 조회
    public Page<FestivalDto> findFestivals(FestivalSearchCond cond, Pageable pageable) {
        return festivalRepository.searchFestival(cond, pageable);
    }

    // 축제 상세(단 건) 조회
    public FestivalDto findFestival(Long id) {
        return festivalRepository.findFestival(id);
    }
}