package com.techvg.ims.service;

import com.techvg.ims.domain.PurchaseOrderDetails;
import com.techvg.ims.repository.PurchaseOrderDetailsRepository;
import com.techvg.ims.service.dto.PurchaseOrderDetailsDTO;
import com.techvg.ims.service.mapper.PurchaseOrderDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseOrderDetails}.
 */
@Service
@Transactional
public class PurchaseOrderDetailsService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsService.class);

    private final PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    private final PurchaseOrderDetailsMapper purchaseOrderDetailsMapper;

    public PurchaseOrderDetailsService(
        PurchaseOrderDetailsRepository purchaseOrderDetailsRepository,
        PurchaseOrderDetailsMapper purchaseOrderDetailsMapper
    ) {
        this.purchaseOrderDetailsRepository = purchaseOrderDetailsRepository;
        this.purchaseOrderDetailsMapper = purchaseOrderDetailsMapper;
    }

    /**
     * Save a purchaseOrderDetails.
     *
     * @param purchaseOrderDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrderDetailsDTO save(PurchaseOrderDetailsDTO purchaseOrderDetailsDTO) {
        log.debug("Request to save PurchaseOrderDetails : {}", purchaseOrderDetailsDTO);
        PurchaseOrderDetails purchaseOrderDetails = purchaseOrderDetailsMapper.toEntity(purchaseOrderDetailsDTO);
        purchaseOrderDetails = purchaseOrderDetailsRepository.save(purchaseOrderDetails);
        return purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);
    }

    /**
     * Partially update a purchaseOrderDetails.
     *
     * @param purchaseOrderDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseOrderDetailsDTO> partialUpdate(PurchaseOrderDetailsDTO purchaseOrderDetailsDTO) {
        log.debug("Request to partially update PurchaseOrderDetails : {}", purchaseOrderDetailsDTO);

        return purchaseOrderDetailsRepository
            .findById(purchaseOrderDetailsDTO.getId())
            .map(existingPurchaseOrderDetails -> {
                purchaseOrderDetailsMapper.partialUpdate(existingPurchaseOrderDetails, purchaseOrderDetailsDTO);

                return existingPurchaseOrderDetails;
            })
            .map(purchaseOrderDetailsRepository::save)
            .map(purchaseOrderDetailsMapper::toDto);
    }

    /**
     * Get all the purchaseOrderDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrderDetails");
        return purchaseOrderDetailsRepository.findAll(pageable).map(purchaseOrderDetailsMapper::toDto);
    }

    /**
     * Get one purchaseOrderDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderDetailsDTO> findOne(Long id) {
        log.debug("Request to get PurchaseOrderDetails : {}", id);
        return purchaseOrderDetailsRepository.findById(id).map(purchaseOrderDetailsMapper::toDto);
    }

    /**
     * Delete the purchaseOrderDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsRepository.deleteById(id);
    }
}
