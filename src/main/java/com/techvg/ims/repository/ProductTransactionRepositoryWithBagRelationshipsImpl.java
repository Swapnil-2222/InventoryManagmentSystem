package com.techvg.ims.repository;

import com.techvg.ims.domain.ProductTransaction;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProductTransactionRepositoryWithBagRelationshipsImpl implements ProductTransactionRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<ProductTransaction> fetchBagRelationships(Optional<ProductTransaction> productTransaction) {
        return productTransaction.map(this::fetchProducts);
    }

    @Override
    public Page<ProductTransaction> fetchBagRelationships(Page<ProductTransaction> productTransactions) {
        return new PageImpl<>(
            fetchBagRelationships(productTransactions.getContent()),
            productTransactions.getPageable(),
            productTransactions.getTotalElements()
        );
    }

    @Override
    public List<ProductTransaction> fetchBagRelationships(List<ProductTransaction> productTransactions) {
        return Optional.of(productTransactions).map(this::fetchProducts).get();
    }

    ProductTransaction fetchProducts(ProductTransaction result) {
        return entityManager
            .createQuery(
                "select productTransaction from ProductTransaction productTransaction left join fetch productTransaction.products where productTransaction is :productTransaction",
                ProductTransaction.class
            )
            .setParameter("productTransaction", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ProductTransaction> fetchProducts(List<ProductTransaction> productTransactions) {
        return entityManager
            .createQuery(
                "select distinct productTransaction from ProductTransaction productTransaction left join fetch productTransaction.products where productTransaction in :productTransactions",
                ProductTransaction.class
            )
            .setParameter("productTransactions", productTransactions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
