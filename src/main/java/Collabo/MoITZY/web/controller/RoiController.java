package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.domain.ROI;
import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.FestivalService;
import Collabo.MoITZY.web.service.RoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class RoiController {

    private final RoiService roiService;
    private final FestivalService festivalService;

    // 찜하기
    @PostMapping("/mo-itzy/festivals/{festival_id}/like")
    public ResponseDto<?> like(@RequestHeader("Authorization") String token,
                               @PathVariable("festival_id") Long festivalId) {
        return roiService.addLike(token, festivalId);
    }

    // 찜 삭제
    @PostMapping("/mo-itzy/{festival_id}/unlike")
    public ResponseDto<?> unlike(@RequestHeader("Authorization") String token,
                                 @PathVariable("festival_id") Long festivalId) {
        return roiService.deleteLike(token, festivalId);
    }

    // 찜 불러오기
    @GetMapping("/mo-itzy/like")
    public ResponseDto<?> getLikeList(@RequestHeader("Authorization") String token,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ROI> likeList = roiService.getLikeList(token);
        ResponseDto<Page<FestivalDto>> likeFestivals = festivalService.findLikeFestivals(likeList, pageable);
        return ResponseDto.ok(OK, "찜 목록 조회 성공", likeFestivals);
    }
}