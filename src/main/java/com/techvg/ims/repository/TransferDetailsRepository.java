package com.techvg.ims.repository;

import com.techvg.ims.domain.TransferDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransferDetails entity.
 */
@Repository
public interface TransferDetailsRepository extends JpaRepository<TransferDetails, Long>, JpaSpecificationExecutor<TransferDetails> {
    default Optional<TransferDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TransferDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TransferDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct transferDetails from TransferDetails transferDetails left join fetch transferDetails.wareHouse",
        countQuery = "select count(distinct transferDetails) from TransferDetails transferDetails"
    )
    Page<TransferDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct transferDetails from TransferDetails transferDetails left join fetch transferDetails.wareHouse")
    List<TransferDetails> findAllWithToOneRelationships();

    @Query(
        "select transferDetails from TransferDetails transferDetails left join fetch transferDetails.wareHouse where transferDetails.id =:id"
    )
    Optional<TransferDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
