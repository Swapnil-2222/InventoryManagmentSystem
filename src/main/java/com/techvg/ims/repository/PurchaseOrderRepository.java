package com.techvg.ims.repository;

import com.techvg.ims.domain.PurchaseOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseOrder entity.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    default Optional<PurchaseOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchaseOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchaseOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.securityUser",
        countQuery = "select count(distinct purchaseOrder) from PurchaseOrder purchaseOrder"
    )
    Page<PurchaseOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.securityUser")
    List<PurchaseOrder> findAllWithToOneRelationships();

    @Query("select purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.securityUser where purchaseOrder.id =:id")
    Optional<PurchaseOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
