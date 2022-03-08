package com.techvg.ims.repository;

import com.techvg.ims.domain.ProductInventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductInventory entity.
 */
@Repository
public interface ProductInventoryRepository
    extends
        ProductInventoryRepositoryWithBagRelationships, JpaRepository<ProductInventory, Long>, JpaSpecificationExecutor<ProductInventory> {
    default Optional<ProductInventory> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ProductInventory> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ProductInventory> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct productInventory from ProductInventory productInventory left join fetch productInventory.product",
        countQuery = "select count(distinct productInventory) from ProductInventory productInventory"
    )
    Page<ProductInventory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct productInventory from ProductInventory productInventory left join fetch productInventory.product")
    List<ProductInventory> findAllWithToOneRelationships();

    @Query(
        "select productInventory from ProductInventory productInventory left join fetch productInventory.product where productInventory.id =:id"
    )
    Optional<ProductInventory> findOneWithToOneRelationships(@Param("id") Long id);
}
