package com.pharmaprocure.portal.repository;

import com.pharmaprocure.portal.entity.CriticalActionRequestEntity;
import com.pharmaprocure.portal.enums.CriticalActionRequestType;
import com.pharmaprocure.portal.enums.CriticalActionStatus;
import com.pharmaprocure.portal.enums.CriticalActionTargetType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriticalActionRequestRepository extends JpaRepository<CriticalActionRequestEntity, Long> {
    @EntityGraph(attributePaths = {"requestedBy"})
    List<CriticalActionRequestEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"requestedBy"})
    List<CriticalActionRequestEntity> findByRequestedByIdOrderByCreatedAtDesc(Long requestedById);

    @EntityGraph(attributePaths = {"requestedBy"})
    List<CriticalActionRequestEntity> findByRequestedByOrganizationCodeOrderByCreatedAtDesc(String organizationCode);

    @EntityGraph(attributePaths = {"requestedBy"})
    Optional<CriticalActionRequestEntity> findByRequestTypeAndTargetTypeAndTargetIdAndStatusIn(
        CriticalActionRequestType requestType,
        CriticalActionTargetType targetType,
        Long targetId,
        List<CriticalActionStatus> statuses
    );
}
