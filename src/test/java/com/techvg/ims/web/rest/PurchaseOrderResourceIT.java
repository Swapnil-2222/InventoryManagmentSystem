package com.techvg.ims.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.ims.IntegrationTest;
import com.techvg.ims.domain.GoodsRecived;
import com.techvg.ims.domain.ProductInventory;
import com.techvg.ims.domain.PurchaseOrder;
import com.techvg.ims.domain.PurchaseOrderDetails;
import com.techvg.ims.domain.SecurityUser;
import com.techvg.ims.domain.enumeration.OrderType;
import com.techvg.ims.domain.enumeration.Status;
import com.techvg.ims.repository.PurchaseOrderRepository;
import com.techvg.ims.service.criteria.PurchaseOrderCriteria;
import com.techvg.ims.service.dto.PurchaseOrderDTO;
import com.techvg.ims.service.mapper.PurchaseOrderMapper;
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
 * Integration tests for the {@link PurchaseOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderResourceIT {

    private static final Double DEFAULT_TOTAL_PO_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_PO_AMOUNT = 2D;
    private static final Double SMALLER_TOTAL_PO_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_TOTAL_GST_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_GST_AMOUNT = 2D;
    private static final Double SMALLER_TOTAL_GST_AMOUNT = 1D - 1D;

    private static final Instant DEFAULT_EXPECTED_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderType DEFAULT_ORDER_TYPE = OrderType.PURCHASE_ORDER;
    private static final OrderType UPDATED_ORDER_TYPE = OrderType.PRODUCT_QUATATION;

    private static final Status DEFAULT_ORDER_STATUS = Status.REQUESTED;
    private static final Status UPDATED_ORDER_STATUS = Status.APPROVED;

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TERMS_AND_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_TERMS_AND_CONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_1 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_1 = "BBBBBBBBBB";

    private static final String DEFAULT_FREE_FIELD_2 = "AAAAAAAAAA";
    private static final String UPDATED_FREE_FIELD_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/purchase-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .totalPOAmount(DEFAULT_TOTAL_PO_AMOUNT)
            .totalGSTAmount(DEFAULT_TOTAL_GST_AMOUNT)
            .expectedDeliveryDate(DEFAULT_EXPECTED_DELIVERY_DATE)
            .poDate(DEFAULT_PO_DATE)
            .orderType(DEFAULT_ORDER_TYPE)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .clientName(DEFAULT_CLIENT_NAME)
            .clientMobile(DEFAULT_CLIENT_MOBILE)
            .clientEmail(DEFAULT_CLIENT_EMAIL)
            .termsAndCondition(DEFAULT_TERMS_AND_CONDITION)
            .notes(DEFAULT_NOTES)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .freeField1(DEFAULT_FREE_FIELD_1)
            .freeField2(DEFAULT_FREE_FIELD_2);
        return purchaseOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createUpdatedEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .poDate(UPDATED_PO_DATE)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .clientMobile(UPDATED_CLIENT_MOBILE)
            .clientEmail(UPDATED_CLIENT_EMAIL)
            .termsAndCondition(UPDATED_TERMS_AND_CONDITION)
            .notes(UPDATED_NOTES)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        return purchaseOrder;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrder() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();
        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);
        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getTotalPOAmount()).isEqualTo(DEFAULT_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseOrder.getTotalGSTAmount()).isEqualTo(DEFAULT_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseOrder.getExpectedDeliveryDate()).isEqualTo(DEFAULT_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(DEFAULT_PO_DATE);
        assertThat(testPurchaseOrder.getOrderType()).isEqualTo(DEFAULT_ORDER_TYPE);
        assertThat(testPurchaseOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testPurchaseOrder.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testPurchaseOrder.getClientMobile()).isEqualTo(DEFAULT_CLIENT_MOBILE);
        assertThat(testPurchaseOrder.getClientEmail()).isEqualTo(DEFAULT_CLIENT_EMAIL);
        assertThat(testPurchaseOrder.getTermsAndCondition()).isEqualTo(DEFAULT_TERMS_AND_CONDITION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPurchaseOrder.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPurchaseOrder.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrder.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testPurchaseOrder.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void createPurchaseOrderWithExistingId() throws Exception {
        // Create the PurchaseOrder with an existing ID
        purchaseOrder.setId(1L);
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderRepository.findAll().size();
        // set the field null
        purchaseOrder.setLastModified(null);

        // Create the PurchaseOrder, which fails.
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderRepository.findAll().size();
        // set the field null
        purchaseOrder.setLastModifiedBy(null);

        // Create the PurchaseOrder, which fails.
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPOAmount").value(hasItem(DEFAULT_TOTAL_PO_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGSTAmount").value(hasItem(DEFAULT_TOTAL_GST_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].expectedDeliveryDate").value(hasItem(DEFAULT_EXPECTED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].poDate").value(hasItem(DEFAULT_PO_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientMobile").value(hasItem(DEFAULT_CLIENT_MOBILE)))
            .andExpect(jsonPath("$.[*].clientEmail").value(hasItem(DEFAULT_CLIENT_EMAIL)))
            .andExpect(jsonPath("$.[*].termsAndCondition").value(hasItem(DEFAULT_TERMS_AND_CONDITION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));
    }

    @Test
    @Transactional
    void getPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.totalPOAmount").value(DEFAULT_TOTAL_PO_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalGSTAmount").value(DEFAULT_TOTAL_GST_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.expectedDeliveryDate").value(DEFAULT_EXPECTED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.poDate").value(DEFAULT_PO_DATE.toString()))
            .andExpect(jsonPath("$.orderType").value(DEFAULT_ORDER_TYPE.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.clientMobile").value(DEFAULT_CLIENT_MOBILE))
            .andExpect(jsonPath("$.clientEmail").value(DEFAULT_CLIENT_EMAIL))
            .andExpect(jsonPath("$.termsAndCondition").value(DEFAULT_TERMS_AND_CONDITION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.freeField1").value(DEFAULT_FREE_FIELD_1))
            .andExpect(jsonPath("$.freeField2").value(DEFAULT_FREE_FIELD_2));
    }

    @Test
    @Transactional
    void getPurchaseOrdersByIdFiltering() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        Long id = purchaseOrder.getId();

        defaultPurchaseOrderShouldBeFound("id.equals=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.notEquals=" + id);

        defaultPurchaseOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultPurchaseOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount equals to DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.equals=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount equals to UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.equals=" + UPDATED_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount not equals to DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.notEquals=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount not equals to UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.notEquals=" + UPDATED_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount in DEFAULT_TOTAL_PO_AMOUNT or UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.in=" + DEFAULT_TOTAL_PO_AMOUNT + "," + UPDATED_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount equals to UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.in=" + UPDATED_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount is not null
        defaultPurchaseOrderShouldBeFound("totalPOAmount.specified=true");

        // Get all the purchaseOrderList where totalPOAmount is null
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount is greater than or equal to DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount is greater than or equal to UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.greaterThanOrEqual=" + UPDATED_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount is less than or equal to DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.lessThanOrEqual=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount is less than or equal to SMALLER_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.lessThanOrEqual=" + SMALLER_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount is less than DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.lessThan=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount is less than UPDATED_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.lessThan=" + UPDATED_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalPOAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalPOAmount is greater than DEFAULT_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalPOAmount.greaterThan=" + DEFAULT_TOTAL_PO_AMOUNT);

        // Get all the purchaseOrderList where totalPOAmount is greater than SMALLER_TOTAL_PO_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalPOAmount.greaterThan=" + SMALLER_TOTAL_PO_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount equals to DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.equals=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount equals to UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.equals=" + UPDATED_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount not equals to DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.notEquals=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount not equals to UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.notEquals=" + UPDATED_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount in DEFAULT_TOTAL_GST_AMOUNT or UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.in=" + DEFAULT_TOTAL_GST_AMOUNT + "," + UPDATED_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount equals to UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.in=" + UPDATED_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount is not null
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.specified=true");

        // Get all the purchaseOrderList where totalGSTAmount is null
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount is greater than or equal to DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount is greater than or equal to UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.greaterThanOrEqual=" + UPDATED_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount is less than or equal to DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.lessThanOrEqual=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount is less than or equal to SMALLER_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.lessThanOrEqual=" + SMALLER_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount is less than DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.lessThan=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount is less than UPDATED_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.lessThan=" + UPDATED_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalGSTAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalGSTAmount is greater than DEFAULT_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("totalGSTAmount.greaterThan=" + DEFAULT_TOTAL_GST_AMOUNT);

        // Get all the purchaseOrderList where totalGSTAmount is greater than SMALLER_TOTAL_GST_AMOUNT
        defaultPurchaseOrderShouldBeFound("totalGSTAmount.greaterThan=" + SMALLER_TOTAL_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByExpectedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where expectedDeliveryDate equals to DEFAULT_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldBeFound("expectedDeliveryDate.equals=" + DEFAULT_EXPECTED_DELIVERY_DATE);

        // Get all the purchaseOrderList where expectedDeliveryDate equals to UPDATED_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldNotBeFound("expectedDeliveryDate.equals=" + UPDATED_EXPECTED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByExpectedDeliveryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where expectedDeliveryDate not equals to DEFAULT_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldNotBeFound("expectedDeliveryDate.notEquals=" + DEFAULT_EXPECTED_DELIVERY_DATE);

        // Get all the purchaseOrderList where expectedDeliveryDate not equals to UPDATED_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldBeFound("expectedDeliveryDate.notEquals=" + UPDATED_EXPECTED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByExpectedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where expectedDeliveryDate in DEFAULT_EXPECTED_DELIVERY_DATE or UPDATED_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldBeFound(
            "expectedDeliveryDate.in=" + DEFAULT_EXPECTED_DELIVERY_DATE + "," + UPDATED_EXPECTED_DELIVERY_DATE
        );

        // Get all the purchaseOrderList where expectedDeliveryDate equals to UPDATED_EXPECTED_DELIVERY_DATE
        defaultPurchaseOrderShouldNotBeFound("expectedDeliveryDate.in=" + UPDATED_EXPECTED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByExpectedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where expectedDeliveryDate is not null
        defaultPurchaseOrderShouldBeFound("expectedDeliveryDate.specified=true");

        // Get all the purchaseOrderList where expectedDeliveryDate is null
        defaultPurchaseOrderShouldNotBeFound("expectedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPoDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate equals to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.equals=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.equals=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPoDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate not equals to DEFAULT_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.notEquals=" + DEFAULT_PO_DATE);

        // Get all the purchaseOrderList where poDate not equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.notEquals=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPoDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate in DEFAULT_PO_DATE or UPDATED_PO_DATE
        defaultPurchaseOrderShouldBeFound("poDate.in=" + DEFAULT_PO_DATE + "," + UPDATED_PO_DATE);

        // Get all the purchaseOrderList where poDate equals to UPDATED_PO_DATE
        defaultPurchaseOrderShouldNotBeFound("poDate.in=" + UPDATED_PO_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPoDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where poDate is not null
        defaultPurchaseOrderShouldBeFound("poDate.specified=true");

        // Get all the purchaseOrderList where poDate is null
        defaultPurchaseOrderShouldNotBeFound("poDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderType equals to DEFAULT_ORDER_TYPE
        defaultPurchaseOrderShouldBeFound("orderType.equals=" + DEFAULT_ORDER_TYPE);

        // Get all the purchaseOrderList where orderType equals to UPDATED_ORDER_TYPE
        defaultPurchaseOrderShouldNotBeFound("orderType.equals=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderType not equals to DEFAULT_ORDER_TYPE
        defaultPurchaseOrderShouldNotBeFound("orderType.notEquals=" + DEFAULT_ORDER_TYPE);

        // Get all the purchaseOrderList where orderType not equals to UPDATED_ORDER_TYPE
        defaultPurchaseOrderShouldBeFound("orderType.notEquals=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderTypeIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderType in DEFAULT_ORDER_TYPE or UPDATED_ORDER_TYPE
        defaultPurchaseOrderShouldBeFound("orderType.in=" + DEFAULT_ORDER_TYPE + "," + UPDATED_ORDER_TYPE);

        // Get all the purchaseOrderList where orderType equals to UPDATED_ORDER_TYPE
        defaultPurchaseOrderShouldNotBeFound("orderType.in=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderType is not null
        defaultPurchaseOrderShouldBeFound("orderType.specified=true");

        // Get all the purchaseOrderList where orderType is null
        defaultPurchaseOrderShouldNotBeFound("orderType.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultPurchaseOrderShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the purchaseOrderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultPurchaseOrderShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderStatus not equals to DEFAULT_ORDER_STATUS
        defaultPurchaseOrderShouldNotBeFound("orderStatus.notEquals=" + DEFAULT_ORDER_STATUS);

        // Get all the purchaseOrderList where orderStatus not equals to UPDATED_ORDER_STATUS
        defaultPurchaseOrderShouldBeFound("orderStatus.notEquals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultPurchaseOrderShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the purchaseOrderList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultPurchaseOrderShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderStatus is not null
        defaultPurchaseOrderShouldBeFound("orderStatus.specified=true");

        // Get all the purchaseOrderList where orderStatus is null
        defaultPurchaseOrderShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName equals to DEFAULT_CLIENT_NAME
        defaultPurchaseOrderShouldBeFound("clientName.equals=" + DEFAULT_CLIENT_NAME);

        // Get all the purchaseOrderList where clientName equals to UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldNotBeFound("clientName.equals=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName not equals to DEFAULT_CLIENT_NAME
        defaultPurchaseOrderShouldNotBeFound("clientName.notEquals=" + DEFAULT_CLIENT_NAME);

        // Get all the purchaseOrderList where clientName not equals to UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldBeFound("clientName.notEquals=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName in DEFAULT_CLIENT_NAME or UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldBeFound("clientName.in=" + DEFAULT_CLIENT_NAME + "," + UPDATED_CLIENT_NAME);

        // Get all the purchaseOrderList where clientName equals to UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldNotBeFound("clientName.in=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName is not null
        defaultPurchaseOrderShouldBeFound("clientName.specified=true");

        // Get all the purchaseOrderList where clientName is null
        defaultPurchaseOrderShouldNotBeFound("clientName.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName contains DEFAULT_CLIENT_NAME
        defaultPurchaseOrderShouldBeFound("clientName.contains=" + DEFAULT_CLIENT_NAME);

        // Get all the purchaseOrderList where clientName contains UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldNotBeFound("clientName.contains=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientNameNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientName does not contain DEFAULT_CLIENT_NAME
        defaultPurchaseOrderShouldNotBeFound("clientName.doesNotContain=" + DEFAULT_CLIENT_NAME);

        // Get all the purchaseOrderList where clientName does not contain UPDATED_CLIENT_NAME
        defaultPurchaseOrderShouldBeFound("clientName.doesNotContain=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile equals to DEFAULT_CLIENT_MOBILE
        defaultPurchaseOrderShouldBeFound("clientMobile.equals=" + DEFAULT_CLIENT_MOBILE);

        // Get all the purchaseOrderList where clientMobile equals to UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldNotBeFound("clientMobile.equals=" + UPDATED_CLIENT_MOBILE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile not equals to DEFAULT_CLIENT_MOBILE
        defaultPurchaseOrderShouldNotBeFound("clientMobile.notEquals=" + DEFAULT_CLIENT_MOBILE);

        // Get all the purchaseOrderList where clientMobile not equals to UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldBeFound("clientMobile.notEquals=" + UPDATED_CLIENT_MOBILE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile in DEFAULT_CLIENT_MOBILE or UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldBeFound("clientMobile.in=" + DEFAULT_CLIENT_MOBILE + "," + UPDATED_CLIENT_MOBILE);

        // Get all the purchaseOrderList where clientMobile equals to UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldNotBeFound("clientMobile.in=" + UPDATED_CLIENT_MOBILE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile is not null
        defaultPurchaseOrderShouldBeFound("clientMobile.specified=true");

        // Get all the purchaseOrderList where clientMobile is null
        defaultPurchaseOrderShouldNotBeFound("clientMobile.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile contains DEFAULT_CLIENT_MOBILE
        defaultPurchaseOrderShouldBeFound("clientMobile.contains=" + DEFAULT_CLIENT_MOBILE);

        // Get all the purchaseOrderList where clientMobile contains UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldNotBeFound("clientMobile.contains=" + UPDATED_CLIENT_MOBILE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientMobileNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientMobile does not contain DEFAULT_CLIENT_MOBILE
        defaultPurchaseOrderShouldNotBeFound("clientMobile.doesNotContain=" + DEFAULT_CLIENT_MOBILE);

        // Get all the purchaseOrderList where clientMobile does not contain UPDATED_CLIENT_MOBILE
        defaultPurchaseOrderShouldBeFound("clientMobile.doesNotContain=" + UPDATED_CLIENT_MOBILE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail equals to DEFAULT_CLIENT_EMAIL
        defaultPurchaseOrderShouldBeFound("clientEmail.equals=" + DEFAULT_CLIENT_EMAIL);

        // Get all the purchaseOrderList where clientEmail equals to UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldNotBeFound("clientEmail.equals=" + UPDATED_CLIENT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail not equals to DEFAULT_CLIENT_EMAIL
        defaultPurchaseOrderShouldNotBeFound("clientEmail.notEquals=" + DEFAULT_CLIENT_EMAIL);

        // Get all the purchaseOrderList where clientEmail not equals to UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldBeFound("clientEmail.notEquals=" + UPDATED_CLIENT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail in DEFAULT_CLIENT_EMAIL or UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldBeFound("clientEmail.in=" + DEFAULT_CLIENT_EMAIL + "," + UPDATED_CLIENT_EMAIL);

        // Get all the purchaseOrderList where clientEmail equals to UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldNotBeFound("clientEmail.in=" + UPDATED_CLIENT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail is not null
        defaultPurchaseOrderShouldBeFound("clientEmail.specified=true");

        // Get all the purchaseOrderList where clientEmail is null
        defaultPurchaseOrderShouldNotBeFound("clientEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail contains DEFAULT_CLIENT_EMAIL
        defaultPurchaseOrderShouldBeFound("clientEmail.contains=" + DEFAULT_CLIENT_EMAIL);

        // Get all the purchaseOrderList where clientEmail contains UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldNotBeFound("clientEmail.contains=" + UPDATED_CLIENT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByClientEmailNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where clientEmail does not contain DEFAULT_CLIENT_EMAIL
        defaultPurchaseOrderShouldNotBeFound("clientEmail.doesNotContain=" + DEFAULT_CLIENT_EMAIL);

        // Get all the purchaseOrderList where clientEmail does not contain UPDATED_CLIENT_EMAIL
        defaultPurchaseOrderShouldBeFound("clientEmail.doesNotContain=" + UPDATED_CLIENT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition equals to DEFAULT_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldBeFound("termsAndCondition.equals=" + DEFAULT_TERMS_AND_CONDITION);

        // Get all the purchaseOrderList where termsAndCondition equals to UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.equals=" + UPDATED_TERMS_AND_CONDITION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition not equals to DEFAULT_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.notEquals=" + DEFAULT_TERMS_AND_CONDITION);

        // Get all the purchaseOrderList where termsAndCondition not equals to UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldBeFound("termsAndCondition.notEquals=" + UPDATED_TERMS_AND_CONDITION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition in DEFAULT_TERMS_AND_CONDITION or UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldBeFound("termsAndCondition.in=" + DEFAULT_TERMS_AND_CONDITION + "," + UPDATED_TERMS_AND_CONDITION);

        // Get all the purchaseOrderList where termsAndCondition equals to UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.in=" + UPDATED_TERMS_AND_CONDITION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition is not null
        defaultPurchaseOrderShouldBeFound("termsAndCondition.specified=true");

        // Get all the purchaseOrderList where termsAndCondition is null
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition contains DEFAULT_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldBeFound("termsAndCondition.contains=" + DEFAULT_TERMS_AND_CONDITION);

        // Get all the purchaseOrderList where termsAndCondition contains UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.contains=" + UPDATED_TERMS_AND_CONDITION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTermsAndConditionNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where termsAndCondition does not contain DEFAULT_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldNotBeFound("termsAndCondition.doesNotContain=" + DEFAULT_TERMS_AND_CONDITION);

        // Get all the purchaseOrderList where termsAndCondition does not contain UPDATED_TERMS_AND_CONDITION
        defaultPurchaseOrderShouldBeFound("termsAndCondition.doesNotContain=" + UPDATED_TERMS_AND_CONDITION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes equals to DEFAULT_NOTES
        defaultPurchaseOrderShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes equals to UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes not equals to DEFAULT_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes not equals to UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the purchaseOrderList where notes equals to UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes is not null
        defaultPurchaseOrderShouldBeFound("notes.specified=true");

        // Get all the purchaseOrderList where notes is null
        defaultPurchaseOrderShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes contains DEFAULT_NOTES
        defaultPurchaseOrderShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes contains UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes does not contain DEFAULT_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes does not contain UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultPurchaseOrderShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the purchaseOrderList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultPurchaseOrderShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the purchaseOrderList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultPurchaseOrderShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the purchaseOrderList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultPurchaseOrderShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModified is not null
        defaultPurchaseOrderShouldBeFound("lastModified.specified=true");

        // Get all the purchaseOrderList where lastModified is null
        defaultPurchaseOrderShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the purchaseOrderList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy is not null
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.specified=true");

        // Get all the purchaseOrderList where lastModifiedBy is null
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the purchaseOrderList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultPurchaseOrderShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1IsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 equals to DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderShouldBeFound("freeField1.equals=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldNotBeFound("freeField1.equals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 not equals to DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderShouldNotBeFound("freeField1.notEquals=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderList where freeField1 not equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldBeFound("freeField1.notEquals=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1IsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 in DEFAULT_FREE_FIELD_1 or UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldBeFound("freeField1.in=" + DEFAULT_FREE_FIELD_1 + "," + UPDATED_FREE_FIELD_1);

        // Get all the purchaseOrderList where freeField1 equals to UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldNotBeFound("freeField1.in=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1IsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 is not null
        defaultPurchaseOrderShouldBeFound("freeField1.specified=true");

        // Get all the purchaseOrderList where freeField1 is null
        defaultPurchaseOrderShouldNotBeFound("freeField1.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1ContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 contains DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderShouldBeFound("freeField1.contains=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderList where freeField1 contains UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldNotBeFound("freeField1.contains=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField1NotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField1 does not contain DEFAULT_FREE_FIELD_1
        defaultPurchaseOrderShouldNotBeFound("freeField1.doesNotContain=" + DEFAULT_FREE_FIELD_1);

        // Get all the purchaseOrderList where freeField1 does not contain UPDATED_FREE_FIELD_1
        defaultPurchaseOrderShouldBeFound("freeField1.doesNotContain=" + UPDATED_FREE_FIELD_1);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2IsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 equals to DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderShouldBeFound("freeField2.equals=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldNotBeFound("freeField2.equals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 not equals to DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderShouldNotBeFound("freeField2.notEquals=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderList where freeField2 not equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldBeFound("freeField2.notEquals=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2IsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 in DEFAULT_FREE_FIELD_2 or UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldBeFound("freeField2.in=" + DEFAULT_FREE_FIELD_2 + "," + UPDATED_FREE_FIELD_2);

        // Get all the purchaseOrderList where freeField2 equals to UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldNotBeFound("freeField2.in=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2IsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 is not null
        defaultPurchaseOrderShouldBeFound("freeField2.specified=true");

        // Get all the purchaseOrderList where freeField2 is null
        defaultPurchaseOrderShouldNotBeFound("freeField2.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2ContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 contains DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderShouldBeFound("freeField2.contains=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderList where freeField2 contains UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldNotBeFound("freeField2.contains=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFreeField2NotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where freeField2 does not contain DEFAULT_FREE_FIELD_2
        defaultPurchaseOrderShouldNotBeFound("freeField2.doesNotContain=" + DEFAULT_FREE_FIELD_2);

        // Get all the purchaseOrderList where freeField2 does not contain UPDATED_FREE_FIELD_2
        defaultPurchaseOrderShouldBeFound("freeField2.doesNotContain=" + UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        PurchaseOrderDetails purchaseOrderDetails;
        if (TestUtil.findAll(em, PurchaseOrderDetails.class).isEmpty()) {
            purchaseOrderDetails = PurchaseOrderDetailsResourceIT.createEntity(em);
            em.persist(purchaseOrderDetails);
            em.flush();
        } else {
            purchaseOrderDetails = TestUtil.findAll(em, PurchaseOrderDetails.class).get(0);
        }
        em.persist(purchaseOrderDetails);
        em.flush();
        purchaseOrder.addPurchaseOrderDetails(purchaseOrderDetails);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long purchaseOrderDetailsId = purchaseOrderDetails.getId();

        // Get all the purchaseOrderList where purchaseOrderDetails equals to purchaseOrderDetailsId
        defaultPurchaseOrderShouldBeFound("purchaseOrderDetailsId.equals=" + purchaseOrderDetailsId);

        // Get all the purchaseOrderList where purchaseOrderDetails equals to (purchaseOrderDetailsId + 1)
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDetailsId.equals=" + (purchaseOrderDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByGoodRecivedIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        GoodsRecived goodRecived;
        if (TestUtil.findAll(em, GoodsRecived.class).isEmpty()) {
            goodRecived = GoodsRecivedResourceIT.createEntity(em);
            em.persist(goodRecived);
            em.flush();
        } else {
            goodRecived = TestUtil.findAll(em, GoodsRecived.class).get(0);
        }
        em.persist(goodRecived);
        em.flush();
        purchaseOrder.addGoodRecived(goodRecived);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long goodRecivedId = goodRecived.getId();

        // Get all the purchaseOrderList where goodRecived equals to goodRecivedId
        defaultPurchaseOrderShouldBeFound("goodRecivedId.equals=" + goodRecivedId);

        // Get all the purchaseOrderList where goodRecived equals to (goodRecivedId + 1)
        defaultPurchaseOrderShouldNotBeFound("goodRecivedId.equals=" + (goodRecivedId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersBySecurityUserIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        SecurityUser securityUser;
        if (TestUtil.findAll(em, SecurityUser.class).isEmpty()) {
            securityUser = SecurityUserResourceIT.createEntity(em);
            em.persist(securityUser);
            em.flush();
        } else {
            securityUser = TestUtil.findAll(em, SecurityUser.class).get(0);
        }
        em.persist(securityUser);
        em.flush();
        purchaseOrder.setSecurityUser(securityUser);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long securityUserId = securityUser.getId();

        // Get all the purchaseOrderList where securityUser equals to securityUserId
        defaultPurchaseOrderShouldBeFound("securityUserId.equals=" + securityUserId);

        // Get all the purchaseOrderList where securityUser equals to (securityUserId + 1)
        defaultPurchaseOrderShouldNotBeFound("securityUserId.equals=" + (securityUserId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByProductInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        ProductInventory productInventory;
        if (TestUtil.findAll(em, ProductInventory.class).isEmpty()) {
            productInventory = ProductInventoryResourceIT.createEntity(em);
            em.persist(productInventory);
            em.flush();
        } else {
            productInventory = TestUtil.findAll(em, ProductInventory.class).get(0);
        }
        em.persist(productInventory);
        em.flush();
        purchaseOrder.addProductInventory(productInventory);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long productInventoryId = productInventory.getId();

        // Get all the purchaseOrderList where productInventory equals to productInventoryId
        defaultPurchaseOrderShouldBeFound("productInventoryId.equals=" + productInventoryId);

        // Get all the purchaseOrderList where productInventory equals to (productInventoryId + 1)
        defaultPurchaseOrderShouldNotBeFound("productInventoryId.equals=" + (productInventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderShouldBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPOAmount").value(hasItem(DEFAULT_TOTAL_PO_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGSTAmount").value(hasItem(DEFAULT_TOTAL_GST_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].expectedDeliveryDate").value(hasItem(DEFAULT_EXPECTED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].poDate").value(hasItem(DEFAULT_PO_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientMobile").value(hasItem(DEFAULT_CLIENT_MOBILE)))
            .andExpect(jsonPath("$.[*].clientEmail").value(hasItem(DEFAULT_CLIENT_EMAIL)))
            .andExpect(jsonPath("$.[*].termsAndCondition").value(hasItem(DEFAULT_TERMS_AND_CONDITION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].freeField1").value(hasItem(DEFAULT_FREE_FIELD_1)))
            .andExpect(jsonPath("$.[*].freeField2").value(hasItem(DEFAULT_FREE_FIELD_2)));

        // Check, that the count call also returns 1
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrder are not directly saved in db
        em.detach(updatedPurchaseOrder);
        updatedPurchaseOrder
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .poDate(UPDATED_PO_DATE)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .clientMobile(UPDATED_CLIENT_MOBILE)
            .clientEmail(UPDATED_CLIENT_EMAIL)
            .termsAndCondition(UPDATED_TERMS_AND_CONDITION)
            .notes(UPDATED_NOTES)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(updatedPurchaseOrder);

        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getTotalPOAmount()).isEqualTo(UPDATED_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseOrder.getTotalGSTAmount()).isEqualTo(UPDATED_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseOrder.getExpectedDeliveryDate()).isEqualTo(UPDATED_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(UPDATED_PO_DATE);
        assertThat(testPurchaseOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testPurchaseOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testPurchaseOrder.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testPurchaseOrder.getClientMobile()).isEqualTo(UPDATED_CLIENT_MOBILE);
        assertThat(testPurchaseOrder.getClientEmail()).isEqualTo(UPDATED_CLIENT_EMAIL);
        assertThat(testPurchaseOrder.getTermsAndCondition()).isEqualTo(UPDATED_TERMS_AND_CONDITION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPurchaseOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrder.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testPurchaseOrder.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .clientEmail(UPDATED_CLIENT_EMAIL)
            .termsAndCondition(UPDATED_TERMS_AND_CONDITION)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getTotalPOAmount()).isEqualTo(DEFAULT_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseOrder.getTotalGSTAmount()).isEqualTo(DEFAULT_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseOrder.getExpectedDeliveryDate()).isEqualTo(UPDATED_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(DEFAULT_PO_DATE);
        assertThat(testPurchaseOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testPurchaseOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testPurchaseOrder.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testPurchaseOrder.getClientMobile()).isEqualTo(DEFAULT_CLIENT_MOBILE);
        assertThat(testPurchaseOrder.getClientEmail()).isEqualTo(UPDATED_CLIENT_EMAIL);
        assertThat(testPurchaseOrder.getTermsAndCondition()).isEqualTo(UPDATED_TERMS_AND_CONDITION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPurchaseOrder.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testPurchaseOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrder.getFreeField1()).isEqualTo(DEFAULT_FREE_FIELD_1);
        assertThat(testPurchaseOrder.getFreeField2()).isEqualTo(DEFAULT_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .poDate(UPDATED_PO_DATE)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .clientMobile(UPDATED_CLIENT_MOBILE)
            .clientEmail(UPDATED_CLIENT_EMAIL)
            .termsAndCondition(UPDATED_TERMS_AND_CONDITION)
            .notes(UPDATED_NOTES)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .freeField1(UPDATED_FREE_FIELD_1)
            .freeField2(UPDATED_FREE_FIELD_2);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getTotalPOAmount()).isEqualTo(UPDATED_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseOrder.getTotalGSTAmount()).isEqualTo(UPDATED_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseOrder.getExpectedDeliveryDate()).isEqualTo(UPDATED_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseOrder.getPoDate()).isEqualTo(UPDATED_PO_DATE);
        assertThat(testPurchaseOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testPurchaseOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testPurchaseOrder.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testPurchaseOrder.getClientMobile()).isEqualTo(UPDATED_CLIENT_MOBILE);
        assertThat(testPurchaseOrder.getClientEmail()).isEqualTo(UPDATED_CLIENT_EMAIL);
        assertThat(testPurchaseOrder.getTermsAndCondition()).isEqualTo(UPDATED_TERMS_AND_CONDITION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testPurchaseOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchaseOrder.getFreeField1()).isEqualTo(UPDATED_FREE_FIELD_1);
        assertThat(testPurchaseOrder.getFreeField2()).isEqualTo(UPDATED_FREE_FIELD_2);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

        // Delete the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
