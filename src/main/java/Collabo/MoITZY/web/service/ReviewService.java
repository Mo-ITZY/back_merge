package Collabo.MoITZY.web.service;

import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.dto.ReviewDto;
import Collabo.MoITZY.web.repository.ReviewRepository;
import Collabo.MoITZY.web.validation.form.ReviewWriteForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    @Transactional
    public ResponseDto<Void> writeReview(Long festivalId, ReviewWriteForm form) {
        reviewRepository.writeReview(festivalId, form.getContent(), form.getImg());
        return ResponseDto.ok(OK, "리뷰 작성 성공");
    }

    // 리뷰 조회
    public ResponseDto<List<ReviewDto>> findReviewOfFestival(Long festivalId) {
        List<ReviewDto> reviewDtos = reviewRepository.findReviewDtos(festivalId);
        return ResponseDto.ok(OK, "리뷰 조회 성공", reviewDtos);
    }
}