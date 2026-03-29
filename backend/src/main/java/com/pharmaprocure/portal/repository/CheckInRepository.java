package com.pharmaprocure.portal.repository;

import com.pharmaprocure.portal.entity.CheckInEntity;
import com.pharmaprocure.portal.enums.RoleName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckInEntity, Long> {
    @EntityGraph(attributePaths = {"ownerUser"})
    List<CheckInEntity> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = {"ownerUser"})
    List<CheckInEntity> findByOwnerUserIdOrderByUpdatedAtDesc(Long ownerUserId);

    @EntityGraph(attributePaths = {"ownerUser"})
    List<CheckInEntity> findByOwnerUserRoleNameOrderByUpdatedAtDesc(RoleName roleName);

    @EntityGraph(attributePaths = {"ownerUser"})
    List<CheckInEntity> findByOwnerUserOrganizationCodeOrderByUpdatedAtDesc(String organizationCode);

    @EntityGraph(attributePaths = {"ownerUser"})
    Optional<CheckInEntity> findWithOwnerUserById(Long id);
}
