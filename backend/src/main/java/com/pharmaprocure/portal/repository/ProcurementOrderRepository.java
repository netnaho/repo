package com.pharmaprocure.portal.repository;

import com.pharmaprocure.portal.entity.ProcurementOrderEntity;
import com.pharmaprocure.portal.enums.RoleName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcurementOrderRepository extends JpaRepository<ProcurementOrderEntity, Long> {
    @EntityGraph(attributePaths = {"buyer", "items", "items.product"})
    Optional<ProcurementOrderEntity> findWithItemsById(Long id);

    @EntityGraph(attributePaths = {"buyer", "items", "items.product"})
    List<ProcurementOrderEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"buyer", "items", "items.product"})
    List<ProcurementOrderEntity> findByBuyerRoleNameOrderByCreatedAtDesc(RoleName roleName);

    @EntityGraph(attributePaths = {"buyer", "items", "items.product"})
    List<ProcurementOrderEntity> findByBuyerOrganizationCodeOrderByCreatedAtDesc(String organizationCode);

    @EntityGraph(attributePaths = {"buyer", "items", "items.product"})
    List<ProcurementOrderEntity> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
}
