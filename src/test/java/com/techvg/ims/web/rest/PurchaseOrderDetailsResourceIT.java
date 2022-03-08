package com.techvg.ims.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.ims.IntegrationTest;
import com.techvg.ims.domain.Product;
import com.techvg.ims.domain.PurchaseOrder;
import com.techvg.ims.domain.PurchaseOrderDetails;
import com.techvg.ims.repository.PurchaseOrderDetailsRepository;
import com.techvg.ims.service.criteria.PurchaseOrderDetailsCriteria;
import com.techvg.ims.service.dto.PurchaseOrderDetailsDTO;
import com.techvg.ims.service.mapper.PurchaseOrderDetailsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PurchaseOrderDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderDetailsResourceIT {

    private static final Double DEFAULT_QTYORDERED = 1D;
    private static final Double UPDATED_QTYORDERED = 2D;
    private static final Double SMALLER_QTYORDERED = 1D - 1D;

    private static final Integer DEFAULT_GST_TAX_PERCENTAGE = 1;
    private static final Integer UPDATED_GST_TAX_PERCENTAGE = 2;
    private static final Integer SMALLER_GST_TAX_PERCENTAGE = 1 - 1;

    private static final Double DEFAULT_PRICE_PER_UNIT = 1D;
    private static final Double UPDATED_PRICE_PER_UNIT = 2D;
    private static final Double SMALLER_PRICE_PER_UNIT = 1D - 1D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_1 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_1 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_2 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/purchase-order-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    @Autowired
    private PurchaseOrderDetailsMapper purchaseOrderDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderDetailsMockMvc;

    private PurchaseOrderDetails purchaseOrderDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderDetails createEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .qtyordered(DEFAULT_QTYORDERED)
            .gstTaxPercentage(DEFAULT_GST_TAX_PERCENTAGE)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .freeField1(DEFAULT_FREE_FIELD_1)
            .freeField2(DEFAULT_FREE_FIELD_2);
        return purchaseOrderDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderDetails createUpdatedEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .qtyordered(UPDATED_QTYORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        return purchaseOrderDetails;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrderDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();
        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);
        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getQtyordered()).isEqualTo(DEFAULT_QTYORDERED);
        assertThat(testPurchaseOrderDetails.getGstTaxPercentage()).isEqualTo(DEFAULT_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseOrderDetails.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testPurchaseOrderDetails.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPurchaseOrderDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrderDetails.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testPurchaseOrderDetails.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void createPurchaseOrderDetailsWithExistingId() throws Exception {
        // Create the PurchaseOrderDetails with an existing ID
        purchaseOrderDetails.setId(1L);
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderDetailsRepository.findAll().size();
        // set the field null
        purchaseOrderDetails.setLastModified(null);

        // Create the PurchaseOrderDetails, which fails.
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderDetailsRepository.findAll().size();
        // set the field null
        purchaseOrderDetails.setLastModifiedBy(null);

        // Create the PurchaseOrderDetails, which fails.
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtyordered").value(hasItem(DEFAULT_QTYORDERED.doubleValue())))
            .andExpect(jsonPath("$.[*].gstTaxPercentage").value(hasItem(DEFAULT_GST_TAX_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));
    }

    @Test
    @Transactional
    void getPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderDetails.getId().intValue()))
            .andExpect(jsonPath("$.qtyordered").value(DEFAULT_QTYORDERED.doubleValue()))
            .andExpect(jsonPath("$.gstTaxPercentage").value(DEFAULT_GST_TAX_PERCENTAGE))
            .andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.freeField1").value(DEFAULT_FREE_FIELD_1))
            .andExpect(jsonPath("$.freeField2").value(DEFAULT_FREE_FIELD_2));
    }

    @Test
    @Transactional
    void getPurchaseOrderDetailsByIdFiltering() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        Long id = purchaseOrderDetails.getId();

        defaultPurchaseOrderDetailsShouldBeFound("id.equals=" + id);
        defaultPurchaseOrderDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultPurchaseOrderDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPurchaseOrderDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultPurchaseOrderDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPurchaseOrderDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered equals to DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.equals=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered equals to UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.equals=" + UPDATED_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered not equals to DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.notEquals=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered not equals to UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.notEquals=" + UPDATED_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered in DEFAULT_QTYORDERED or UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.in=" + DEFAULT_QTYORDERED + "," + UPDATED_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered equals to UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.in=" + UPDATED_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered is not null
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.specified=true");

        // Get all the purchaseOrderDetailsList where qtyordered is null
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered is greater than or equal to DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.greaterThanOrEqual=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered is greater than or equal to UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.greaterThanOrEqual=" + UPDATED_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered is less than or equal to DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.lessThanOrEqual=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered is less than or equal to SMALLER_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.lessThanOrEqual=" + SMALLER_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered is less than DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.lessThan=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered is less than UPDATED_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.lessThan=" + UPDATED_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByQtyorderedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where qtyordered is greater than DEFAULT_QTYORDERED
        defaultPurchaseOrderDetailsShouldNotBeFound("qtyordered.greaterThan=" + DEFAULT_QTYORDERED);

        // Get all the purchaseOrderDetailsList where qtyordered is greater than SMALLER_QTYORDERED
        defaultPurchaseOrderDetailsShouldBeFound("qtyordered.greaterThan=" + SMALLER_QTYORDERED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage equals to DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.equals=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage equals to UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.equals=" + UPDATED_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage not equals to DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.notEquals=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage not equals to UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.notEquals=" + UPDATED_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage in DEFAULT_GST_TAX_PERCENTAGE or UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.in=" + DEFAULT_GST_TAX_PERCENTAGE + "," + UPDATED_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage equals to UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.in=" + UPDATED_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is not null
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.specified=true");

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is null
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is greater than or equal to DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.greaterThanOrEqual=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is greater than or equal to UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.greaterThanOrEqual=" + UPDATED_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is less than or equal to DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.lessThanOrEqual=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is less than or equal to SMALLER_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.lessThanOrEqual=" + SMALLER_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is less than DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.lessThan=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is less than UPDATED_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.lessThan=" + UPDATED_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByGstTaxPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is greater than DEFAULT_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldNotBeFound("gstTaxPercentage.greaterThan=" + DEFAULT_GST_TAX_PERCENTAGE);

        // Get all the purchaseOrderDetailsList where gstTaxPercentage is greater than SMALLER_GST_TAX_PERCENTAGE
        defaultPurchaseOrderDetailsShouldBeFound("gstTaxPercentage.greaterThan=" + SMALLER_GST_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit not equals to DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.notEquals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit not equals to UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.notEquals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit is not null
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.specified=true");

        // Get all the purchaseOrderDetailsList where pricePerUnit is null
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the purchaseOrderDetailsList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultPurchaseOrderDetailsShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice not equals to DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.notEquals=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice not equals to UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.notEquals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice is not null
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.specified=true");

        // Get all the purchaseOrderDetailsList where totalPrice is null
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the purchaseOrderDetailsList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultPurchaseOrderDetailsShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount equals to DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount equals to UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount not equals to DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.notEquals=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount not equals to UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.notEquals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount equals to UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount is not null
        defaultPurchaseOrderDetailsShouldBeFound("discount.specified=true");

        // Get all the purchaseOrderDetailsList where discount is null
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount is less than or equal to SMALLER_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount is less than DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount is less than UPDATED_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where discount is greater than DEFAULT_DISCOUNT
        defaultPurchaseOrderDetailsShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the purchaseOrderDetailsList where discount is greater than SMALLER_DISCOUNT
        defaultPurchaseOrderDetailsShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the purchaseOrderDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the purchaseOrderDetailsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the purchaseOrderDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModified is not null
        defaultPurchaseOrderDetailsShouldBeFound("lastModified.specified=true");

        // Get all the purchaseOrderDetailsList where lastModified is null
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderDetailsList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the purchaseOrderDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy is not null
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the purchaseOrderDetailsList where lastModifiedBy is null
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderDetailsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderDetailsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderDetailsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1IsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 equals to DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.equals=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderDetailsList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.equals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 not equals to DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.notEquals=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderDetailsList where freeField1 not equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.notEquals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1IsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 in DEFAULT_FREE_FIELD_1 or UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.in=" + DEFAULT_FREE_FIELD_1 + "," + UPDATED_FREE_FIELD_1);

        // Get all the purchaseOrderDetailsList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.in=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1IsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 is not null
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.specified=true");

        // Get all the purchaseOrderDetailsList where freeField1 is null
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1ContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 contains DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.contains=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderDetailsList where freeField1 contains UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.contains=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField1NotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField1 does not contain DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField1.doesNotContain=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderDetailsList where freeField1 does not contain UPDATED_FREE_FIELD_1
        defaultPurchaseOrderDetailsShouldBeFound("freeField1.doesNotContain=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2IsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 equals to DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.equals=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderDetailsList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.equals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 not equals to DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.notEquals=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderDetailsList where freeField2 not equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.notEquals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2IsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 in DEFAULT_FREE_FIELD_2 or UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.in=" + DEFAULT_FREE_FIELD_2 + "," + UPDATED_FREE_FIELD_2);

        // Get all the purchaseOrderDetailsList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.in=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2IsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 is not null
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.specified=true");

        // Get all the purchaseOrderDetailsList where freeField2 is null
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2ContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 contains DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.contains=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderDetailsList where freeField2 contains UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.contains=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByFreeField2NotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList where freeField2 does not contain DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldNotBeFound("freeField2.doesNotContain=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderDetailsList where freeField2 does not contain UPDATED_FREE_FIELD_2
        defaultPurchaseOrderDetailsShouldBeFound("freeField2.doesNotContain=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        purchaseOrderDetails.addProduct(product);
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Long productId = product.getId();

        // Get all the purchaseOrderDetailsList where product equals to productId
        defaultPurchaseOrderDetailsShouldBeFound("productId.equals=" + productId);

        // Get all the purchaseOrderDetailsList where product equals to (productId + 1)
        defaultPurchaseOrderDetailsShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetailsByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        purchaseOrderDetails.setPurchaseOrder(purchaseOrder);
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the purchaseOrderDetailsList where purchaseOrder equals to purchaseOrderId
        defaultPurchaseOrderDetailsShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the purchaseOrderDetailsList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultPurchaseOrderDetailsShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderDetailsShouldBeFound(String filter) throws Exception {
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtyordered").value(hasItem(DEFAULT_QTYORDERED.doubleValue())))
            .andExpect(jsonPath("$.[*].gstTaxPercentage").value(hasItem(DEFAULT_GST_TAX_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));

        // Check, that the count call also returns 1
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderDetailsShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrderDetails() throws Exception {
        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails
        PurchaseOrderDetails updatedPurchaseOrderDetails = purchaseOrderDetailsRepository.findById(purchaseOrderDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderDetails are not directly saved in db
        em.detach(updatedPurchaseOrderDetails);
        updatedPurchaseOrderDetails
            .qtyordered(UPDATED_QTYORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(updatedPurchaseOrderDetails);

        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getQtyordered()).isEqualTo(UPDATED_QTYORDERED);
        assertThat(testPurchaseOrderDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseOrderDetails.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testPurchaseOrderDetails.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPurchaseOrderDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrderDetails.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testPurchaseOrderDetails.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails using partial update
        PurchaseOrderDetails partialUpdatedPurchaseOrderDetails = new PurchaseOrderDetails();
        partialUpdatedPurchaseOrderDetails.setId(purchaseOrderDetails.getId());

        partialUpdatedPurchaseOrderDetails
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .discount(UPDATED_DISCOUNT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);

        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getQtyordered()).isEqualTo(DEFAULT_QTYORDERED);
        assertThat(testPurchaseOrderDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseOrderDetails.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testPurchaseOrderDetails.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPurchaseOrderDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrderDetails.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testPurchaseOrderDetails.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails using partial update
        PurchaseOrderDetails partialUpdatedPurchaseOrderDetails = new PurchaseOrderDetails();
        partialUpdatedPurchaseOrderDetails.setId(purchaseOrderDetails.getId());

        partialUpdatedPurchaseOrderDetails
            .qtyordered(UPDATED_QTYORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);

        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getQtyordered()).isEqualTo(UPDATED_QTYORDERED);
        assertThat(testPurchaseOrderDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseOrderDetails.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testPurchaseOrderDetails.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPurchaseOrderDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrderDetails.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testPurchaseOrderDetails.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // Create the PurchaseOrderDetails
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = purchaseOrderDetailsMapper.toDto(purchaseOrderDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeDelete = purchaseOrderDetailsRepository.findAll().size();

        // Delete the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrderDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
