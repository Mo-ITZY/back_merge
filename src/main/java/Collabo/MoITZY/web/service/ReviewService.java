package Collabo.MoITZY.web.service;

import Collabo.MoITZY.dto.ReviewDto;
import Collabo.MoITZY.web.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void writeReview() {
        // 리뷰 작성
    }

    // 리뷰 조회
    public List<ReviewDto> findReviewOfFestival(Long festivalId) {
        return reviewRepository.findReviewDtos(festivalId);
    }

}