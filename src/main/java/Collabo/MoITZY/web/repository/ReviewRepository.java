package Collabo.MoITZY.web.repository;

import Collabo.MoITZY.domain.Review;
import Collabo.MoITZY.web.repository.dynamic.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByFestivalIdAndUserId(Long festivalId, Long userId);
}