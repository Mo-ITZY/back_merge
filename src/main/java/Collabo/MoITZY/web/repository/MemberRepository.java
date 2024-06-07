package Collabo.MoITZY.web.repository;

import Collabo.MoITZY.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    public boolean existsByLoginId(String loginId);

    public boolean existsByLoginIdAndPassword(String loginId, String password);
}