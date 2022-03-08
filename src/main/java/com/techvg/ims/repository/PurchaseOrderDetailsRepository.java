package com.techvg.ims.repository;

import com.techvg.ims.domain.PurchaseOrderDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderDetailsRepository
    extends JpaRepository<PurchaseOrderDetails, Long>, JpaSpecificationExecutor<PurchaseOrderDetails> {}
