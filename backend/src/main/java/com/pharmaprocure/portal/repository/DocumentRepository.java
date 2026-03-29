package com.pharmaprocure.portal.repository;

import com.pharmaprocure.portal.entity.DocumentEntity;
import com.pharmaprocure.portal.enums.RoleName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    @EntityGraph(attributePaths = {"documentType", "ownerUser", "template", "currentVersion"})
    List<DocumentEntity> findAllByOrderByUpdatedAtDesc();

    @EntityGraph(attributePaths = {"documentType", "ownerUser", "template", "currentVersion"})
    List<DocumentEntity> findByOwnerUserIdOrderByUpdatedAtDesc(Long ownerUserId);

    @EntityGraph(attributePaths = {"documentType", "ownerUser", "template", "currentVersion"})
    List<DocumentEntity> findByOwnerUserRoleNameOrderByUpdatedAtDesc(RoleName roleName);

    @EntityGraph(attributePaths = {"documentType", "ownerUser", "template", "currentVersion"})
    List<DocumentEntity> findByOwnerUserOrganizationCodeOrderByUpdatedAtDesc(String organizationCode);

    @EntityGraph(attributePaths = {"documentType", "ownerUser", "template", "currentVersion"})
    Optional<DocumentEntity> findWithCurrentVersionById(Long id);
}
