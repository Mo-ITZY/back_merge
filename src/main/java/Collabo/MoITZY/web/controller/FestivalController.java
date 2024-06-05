package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.web.repository.cond.FestivalSearchCond;
import Collabo.MoITZY.web.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @PostMapping("/mo-itzy/festivals")
    public Page<FestivalDto> showFestivals(
            @RequestBody FestivalSearchCond cond,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return festivalService.findFestivals(cond, pageable);
    }
}