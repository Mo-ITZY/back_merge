package Collabo.MoITZY.web.repository;

import Collabo.MoITZY.domain.ROI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoiRepository extends JpaRepository<ROI, Long> {
    boolean existsByUserIdAndFestivalId(Long userId, Long festivalId);

    Optional<ROI> findByUserIdAndFestivalId(Long userId, Long festivalId);
}