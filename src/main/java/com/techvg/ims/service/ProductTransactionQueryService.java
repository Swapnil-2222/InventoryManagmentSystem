package com.techvg.ims.service;

import com.techvg.ims.domain.*; // for static metamodels
import com.techvg.ims.domain.ProductTransaction;
import com.techvg.ims.repository.ProductTransactionRepository;
import com.techvg.ims.service.criteria.ProductTransactionCriteria;
import com.techvg.ims.service.dto.ProductTransactionDTO;
import com.techvg.ims.service.mapper.ProductTransactionMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductTransaction} entities in the database.
 * The main input is a {@link ProductTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductTransactionDTO} or a {@link Page} of {@link ProductTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTransactionQueryService extends QueryService<ProductTransaction> {

    private final Logger log = LoggerFactory.getLogger(ProductTransactionQueryService.class);

    private final ProductTransactionRepository productTransactionRepository;

    private final ProductTransactionMapper productTransactionMapper;

    public ProductTransactionQueryService(
        ProductTransactionRepository productTransactionRepository,
        ProductTransactionMapper productTransactionMapper
    ) {
        this.productTransactionRepository = productTransactionRepository;
        this.productTransactionMapper = productTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link ProductTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTransactionDTO> findByCriteria(ProductTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductTransaction> specification = createSpecification(criteria);
        return productTransactionMapper.toDto(productTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTransactionDTO> findByCriteria(ProductTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductTransaction> specification = createSpecification(criteria);
        return productTransactionRepository.findAll(specification, page).map(productTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductTransaction> specification = createSpecification(criteria);
        return productTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductTransaction> createSpecification(ProductTransactionCriteria criteria) {
        Specification<ProductTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductTransaction_.id));
            }
            if (criteria.getQtySold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQtySold(), ProductTransaction_.qtySold));
            }
            if (criteria.getPricePerUnit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPricePerUnit(), ProductTransaction_.pricePerUnit));
            }
            if (criteria.getLotNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLotNumber(), ProductTransaction_.lotNumber));
            }
            if (criteria.getExpirydate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirydate(), ProductTransaction_.expirydate));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), ProductTransaction_.totalAmount));
            }
            if (criteria.getGstAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGstAmount(), ProductTransaction_.gstAmount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductTransaction_.description));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), ProductTransaction_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ProductTransaction_.lastModifiedBy));
            }
            if (criteria.getEcurityUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEcurityUserId(),
                            root -> root.join(ProductTransaction_.ecurityUser, JoinType.LEFT).get(SecurityUser_.id)
                        )
                    );
            }
            if (criteria.getWareHouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWareHouseId(),
                            root -> root.join(ProductTransaction_.wareHouse, JoinType.LEFT).get(WareHouse_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
