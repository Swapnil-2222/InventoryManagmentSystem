package com.techvg.ims.web.rest;

import com.techvg.ims.repository.PurchaseOrderDetailsRepository;
import com.techvg.ims.service.PurchaseOrderDetailsQueryService;
import com.techvg.ims.service.PurchaseOrderDetailsService;
import com.techvg.ims.service.criteria.PurchaseOrderDetailsCriteria;
import com.techvg.ims.service.dto.PurchaseOrderDetailsDTO;
import com.techvg.ims.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.techvg.ims.domain.PurchaseOrderDetails}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsResource.class);

    private static final String ENTITY_NAME = "purchaseOrderDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderDetailsService purchaseOrderDetailsService;

    private final PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    private final PurchaseOrderDetailsQueryService purchaseOrderDetailsQueryService;

    public PurchaseOrderDetailsResource(
        PurchaseOrderDetailsService purchaseOrderDetailsService,
        PurchaseOrderDetailsRepository purchaseOrderDetailsRepository,
        PurchaseOrderDetailsQueryService purchaseOrderDetailsQueryService
    ) {
        this.purchaseOrderDetailsService = purchaseOrderDetailsService;
        this.purchaseOrderDetailsRepository = purchaseOrderDetailsRepository;
        this.purchaseOrderDetailsQueryService = purchaseOrderDetailsQueryService;
    }

    /**
     * {@code POST  /purchase-order-details} : Create a new purchaseOrderDetails.
     *
     * @param purchaseOrderDetailsDTO the purchaseOrderDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderDetailsDTO, or with status {@code 400 (Bad Request)} if the purchaseOrderDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-order-details")
    public ResponseEntity<PurchaseOrderDetailsDTO> createPurchaseOrderDetails(
        @Valid @RequestBody PurchaseOrderDetailsDTO purchaseOrderDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderDetails : {}", purchaseOrderDetailsDTO);
        if (purchaseOrderDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderDetailsDTO result = purchaseOrderDetailsService.save(purchaseOrderDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/purchase-order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-order-details/:id} : Updates an existing purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetailsDTO to save.
     * @param purchaseOrderDetailsDTO the purchaseOrderDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-order-details/{id}")
    public ResponseEntity<PurchaseOrderDetailsDTO> updatePurchaseOrderDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchaseOrderDetailsDTO purchaseOrderDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderDetails : {}, {}", id, purchaseOrderDetailsDTO);
        if (purchaseOrderDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseOrderDetailsDTO result = purchaseOrderDetailsService.save(purchaseOrderDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-order-details/:id} : Partial updates given fields of an existing purchaseOrderDetails, field will ignore if it is null
     *
     * @param id the id of the purchaseOrderDetailsDTO to save.
     * @param purchaseOrderDetailsDTO the purchaseOrderDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrderDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-order-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrderDetailsDTO> partialUpdatePurchaseOrderDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchaseOrderDetailsDTO purchaseOrderDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseOrderDetails partially : {}, {}", id, purchaseOrderDetailsDTO);
        if (purchaseOrderDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrderDetailsDTO> result = purchaseOrderDetailsService.partialUpdate(purchaseOrderDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-order-details} : get all the purchaseOrderDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderDetails in body.
     */
    @GetMapping("/purchase-order-details")
    public ResponseEntity<List<PurchaseOrderDetailsDTO>> getAllPurchaseOrderDetails(
        PurchaseOrderDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PurchaseOrderDetails by criteria: {}", criteria);
        Page<PurchaseOrderDetailsDTO> page = purchaseOrderDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-order-details/count} : count all the purchaseOrderDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/purchase-order-details/count")
    public ResponseEntity<Long> countPurchaseOrderDetails(PurchaseOrderDetailsCriteria criteria) {
        log.debug("REST request to count PurchaseOrderDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseOrderDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-order-details/:id} : get the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-order-details/{id}")
    public ResponseEntity<PurchaseOrderDetailsDTO> getPurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderDetails : {}", id);
        Optional<PurchaseOrderDetailsDTO> purchaseOrderDetailsDTO = purchaseOrderDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderDetailsDTO);
    }

    /**
     * {@code DELETE  /purchase-order-details/:id} : delete the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-order-details/{id}")
    public ResponseEntity<Void> deletePurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
