package org.example.gym_application.repository;

import org.example.gym_application.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByMemberId(Long memberId);
    boolean existsByMemberIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long memberId, LocalDate startDate, LocalDate endDate);
}




