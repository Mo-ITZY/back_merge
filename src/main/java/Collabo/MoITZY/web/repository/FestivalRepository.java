package Collabo.MoITZY.web.repository;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.web.repository.dynamic.FestivalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long>, FestivalRepositoryCustom {
}