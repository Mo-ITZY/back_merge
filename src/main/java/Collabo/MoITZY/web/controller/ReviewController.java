package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.ReviewService;
import Collabo.MoITZY.web.validation.form.ReviewWriteForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 페이지 요청
    @GetMapping("mo-itzy/festivals/{festival_id}/review")
    public ResponseDto<?> reviewForm(@PathVariable("festival_id") Long festivalId) {
        return reviewService.findReviewOfFestival(festivalId);
    }

    // 리뷰 작성
    @PostMapping("mo-itzy/festivals/{festival_id}/review")
    public ResponseDto<?> writeReview(@PathVariable("festival_id") Long festivalId, @RequestBody ReviewWriteForm form) {
        return reviewService.writeReview(festivalId, form);
    }
}