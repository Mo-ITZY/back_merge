package Collabo.MoITZY.web.repository;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.web.repository.dynamic.InformRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformRepository extends JpaRepository<Inform, Long>, InformRepositoryCustom {
}