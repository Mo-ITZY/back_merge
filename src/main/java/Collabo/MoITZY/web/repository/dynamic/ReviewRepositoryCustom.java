package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.dto.ReviewDto;

import java.util.List;

public interface ReviewRepositoryCustom {
    public List<ReviewDto> findReviewDtos(Long festivalId);
}