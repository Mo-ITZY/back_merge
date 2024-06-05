package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ReviewDto;
import Collabo.MoITZY.web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 페이지 요청
    @GetMapping("mo-itzy/festivals/{festival_id}/review")
    public List<ReviewDto> reviewForm(@PathVariable("festival_id") Long festivalId) {
        return reviewService.findReviewOfFestival(festivalId);
    }
}