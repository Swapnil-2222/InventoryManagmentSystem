package com.techvg.ims.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.ims.IntegrationTest;
import com.techvg.ims.domain.ProductTransaction;
import com.techvg.ims.domain.SecurityUser;
import com.techvg.ims.domain.WareHouse;
import com.techvg.ims.repository.ProductTransactionRepository;
import com.techvg.ims.service.criteria.ProductTransactionCriteria;
import com.techvg.ims.service.dto.ProductTransactionDTO;
import com.techvg.ims.service.mapper.ProductTransactionMapper;
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
 * Integration tests for the {@link ProductTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTransactionResourceIT {

    private static final Double DEFAULT_QTY_SOLD = 1D;
    private static final Double UPDATED_QTY_SOLD = 2D;
    private static final Double SMALLER_QTY_SOLD = 1D - 1D;

    private static final Double DEFAULT_PRICE_PER_UNIT = 1D;
    private static final Double UPDATED_PRICE_PER_UNIT = 2D;
    private static final Double SMALLER_PRICE_PER_UNIT = 1D - 1D;

    private static final String DEFAULT_LOT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LOT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRYDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRYDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;
    private static final Double SMALLER_TOTAL_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_GST_AMOUNT = 1D;
    private static final Double UPDATED_GST_AMOUNT = 2D;
    private static final Double SMALLER_GST_AMOUNT = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductTransactionRepository productTransactionRepository;

    @Autowired
    private ProductTransactionMapper productTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTransactionMockMvc;

    private ProductTransaction productTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTransaction createEntity(EntityManager em) {
        ProductTransaction productTransaction = new ProductTransaction()
            .qtySold(DEFAULT_QTY_SOLD)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .lotNumber(DEFAULT_LOT_NUMBER)
            .expirydate(DEFAULT_EXPIRYDATE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .gstAmount(DEFAULT_GST_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return productTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTransaction createUpdatedEntity(EntityManager em) {
        ProductTransaction productTransaction = new ProductTransaction()
            .qtySold(UPDATED_QTY_SOLD)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .lotNumber(UPDATED_LOT_NUMBER)
            .expirydate(UPDATED_EXPIRYDATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .gstAmount(UPDATED_GST_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return productTransaction;
    }

    @BeforeEach
    public void initTest() {
        productTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createProductTransaction() throws Exception {
        int databaseSizeBeforeCreate = productTransactionRepository.findAll().size();
        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);
        restProductTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ProductTransaction testProductTransaction = productTransactionList.get(productTransactionList.size() - 1);
        assertThat(testProductTransaction.getQtySold()).isEqualTo(DEFAULT_QTY_SOLD);
        assertThat(testProductTransaction.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testProductTransaction.getLotNumber()).isEqualTo(DEFAULT_LOT_NUMBER);
        assertThat(testProductTransaction.getExpirydate()).isEqualTo(DEFAULT_EXPIRYDATE);
        assertThat(testProductTransaction.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testProductTransaction.getGstAmount()).isEqualTo(DEFAULT_GST_AMOUNT);
        assertThat(testProductTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductTransaction.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testProductTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createProductTransactionWithExistingId() throws Exception {
        // Create the ProductTransaction with an existing ID
        productTransaction.setId(1L);
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        int databaseSizeBeforeCreate = productTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductTransactions() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtySold").value(hasItem(DEFAULT_QTY_SOLD.doubleValue())))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].lotNumber").value(hasItem(DEFAULT_LOT_NUMBER)))
            .andExpect(jsonPath("$.[*].expirydate").value(hasItem(DEFAULT_EXPIRYDATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].gstAmount").value(hasItem(DEFAULT_GST_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getProductTransaction() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get the productTransaction
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, productTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productTransaction.getId().intValue()))
            .andExpect(jsonPath("$.qtySold").value(DEFAULT_QTY_SOLD.doubleValue()))
            .andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
            .andExpect(jsonPath("$.lotNumber").value(DEFAULT_LOT_NUMBER))
            .andExpect(jsonPath("$.expirydate").value(DEFAULT_EXPIRYDATE.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.gstAmount").value(DEFAULT_GST_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getProductTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        Long id = productTransaction.getId();

        defaultProductTransactionShouldBeFound("id.equals=" + id);
        defaultProductTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultProductTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultProductTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold equals to DEFAULT_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.equals=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold equals to UPDATED_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.equals=" + UPDATED_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold not equals to DEFAULT_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.notEquals=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold not equals to UPDATED_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.notEquals=" + UPDATED_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold in DEFAULT_QTY_SOLD or UPDATED_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.in=" + DEFAULT_QTY_SOLD + "," + UPDATED_QTY_SOLD);

        // Get all the productTransactionList where qtySold equals to UPDATED_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.in=" + UPDATED_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold is not null
        defaultProductTransactionShouldBeFound("qtySold.specified=true");

        // Get all the productTransactionList where qtySold is null
        defaultProductTransactionShouldNotBeFound("qtySold.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold is greater than or equal to DEFAULT_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.greaterThanOrEqual=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold is greater than or equal to UPDATED_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.greaterThanOrEqual=" + UPDATED_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold is less than or equal to DEFAULT_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.lessThanOrEqual=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold is less than or equal to SMALLER_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.lessThanOrEqual=" + SMALLER_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsLessThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold is less than DEFAULT_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.lessThan=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold is less than UPDATED_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.lessThan=" + UPDATED_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByQtySoldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where qtySold is greater than DEFAULT_QTY_SOLD
        defaultProductTransactionShouldNotBeFound("qtySold.greaterThan=" + DEFAULT_QTY_SOLD);

        // Get all the productTransactionList where qtySold is greater than SMALLER_QTY_SOLD
        defaultProductTransactionShouldBeFound("qtySold.greaterThan=" + SMALLER_QTY_SOLD);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit not equals to DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.notEquals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit not equals to UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.notEquals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit is not null
        defaultProductTransactionShouldBeFound("pricePerUnit.specified=true");

        // Get all the productTransactionList where pricePerUnit is null
        defaultProductTransactionShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultProductTransactionShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the productTransactionList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultProductTransactionShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber equals to DEFAULT_LOT_NUMBER
        defaultProductTransactionShouldBeFound("lotNumber.equals=" + DEFAULT_LOT_NUMBER);

        // Get all the productTransactionList where lotNumber equals to UPDATED_LOT_NUMBER
        defaultProductTransactionShouldNotBeFound("lotNumber.equals=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber not equals to DEFAULT_LOT_NUMBER
        defaultProductTransactionShouldNotBeFound("lotNumber.notEquals=" + DEFAULT_LOT_NUMBER);

        // Get all the productTransactionList where lotNumber not equals to UPDATED_LOT_NUMBER
        defaultProductTransactionShouldBeFound("lotNumber.notEquals=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber in DEFAULT_LOT_NUMBER or UPDATED_LOT_NUMBER
        defaultProductTransactionShouldBeFound("lotNumber.in=" + DEFAULT_LOT_NUMBER + "," + UPDATED_LOT_NUMBER);

        // Get all the productTransactionList where lotNumber equals to UPDATED_LOT_NUMBER
        defaultProductTransactionShouldNotBeFound("lotNumber.in=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber is not null
        defaultProductTransactionShouldBeFound("lotNumber.specified=true");

        // Get all the productTransactionList where lotNumber is null
        defaultProductTransactionShouldNotBeFound("lotNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber contains DEFAULT_LOT_NUMBER
        defaultProductTransactionShouldBeFound("lotNumber.contains=" + DEFAULT_LOT_NUMBER);

        // Get all the productTransactionList where lotNumber contains UPDATED_LOT_NUMBER
        defaultProductTransactionShouldNotBeFound("lotNumber.contains=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLotNumberNotContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lotNumber does not contain DEFAULT_LOT_NUMBER
        defaultProductTransactionShouldNotBeFound("lotNumber.doesNotContain=" + DEFAULT_LOT_NUMBER);

        // Get all the productTransactionList where lotNumber does not contain UPDATED_LOT_NUMBER
        defaultProductTransactionShouldBeFound("lotNumber.doesNotContain=" + UPDATED_LOT_NUMBER);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByExpirydateIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where expirydate equals to DEFAULT_EXPIRYDATE
        defaultProductTransactionShouldBeFound("expirydate.equals=" + DEFAULT_EXPIRYDATE);

        // Get all the productTransactionList where expirydate equals to UPDATED_EXPIRYDATE
        defaultProductTransactionShouldNotBeFound("expirydate.equals=" + UPDATED_EXPIRYDATE);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByExpirydateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where expirydate not equals to DEFAULT_EXPIRYDATE
        defaultProductTransactionShouldNotBeFound("expirydate.notEquals=" + DEFAULT_EXPIRYDATE);

        // Get all the productTransactionList where expirydate not equals to UPDATED_EXPIRYDATE
        defaultProductTransactionShouldBeFound("expirydate.notEquals=" + UPDATED_EXPIRYDATE);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByExpirydateIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where expirydate in DEFAULT_EXPIRYDATE or UPDATED_EXPIRYDATE
        defaultProductTransactionShouldBeFound("expirydate.in=" + DEFAULT_EXPIRYDATE + "," + UPDATED_EXPIRYDATE);

        // Get all the productTransactionList where expirydate equals to UPDATED_EXPIRYDATE
        defaultProductTransactionShouldNotBeFound("expirydate.in=" + UPDATED_EXPIRYDATE);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByExpirydateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where expirydate is not null
        defaultProductTransactionShouldBeFound("expirydate.specified=true");

        // Get all the productTransactionList where expirydate is null
        defaultProductTransactionShouldNotBeFound("expirydate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount not equals to DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.notEquals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount not equals to UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.notEquals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount is not null
        defaultProductTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the productTransactionList where totalAmount is null
        defaultProductTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultProductTransactionShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the productTransactionList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultProductTransactionShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount equals to DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.equals=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount equals to UPDATED_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.equals=" + UPDATED_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount not equals to DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.notEquals=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount not equals to UPDATED_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.notEquals=" + UPDATED_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount in DEFAULT_GST_AMOUNT or UPDATED_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.in=" + DEFAULT_GST_AMOUNT + "," + UPDATED_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount equals to UPDATED_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.in=" + UPDATED_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount is not null
        defaultProductTransactionShouldBeFound("gstAmount.specified=true");

        // Get all the productTransactionList where gstAmount is null
        defaultProductTransactionShouldNotBeFound("gstAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount is greater than or equal to DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.greaterThanOrEqual=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount is greater than or equal to UPDATED_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.greaterThanOrEqual=" + UPDATED_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount is less than or equal to DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.lessThanOrEqual=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount is less than or equal to SMALLER_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.lessThanOrEqual=" + SMALLER_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount is less than DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.lessThan=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount is less than UPDATED_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.lessThan=" + UPDATED_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByGstAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where gstAmount is greater than DEFAULT_GST_AMOUNT
        defaultProductTransactionShouldNotBeFound("gstAmount.greaterThan=" + DEFAULT_GST_AMOUNT);

        // Get all the productTransactionList where gstAmount is greater than SMALLER_GST_AMOUNT
        defaultProductTransactionShouldBeFound("gstAmount.greaterThan=" + SMALLER_GST_AMOUNT);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description equals to DEFAULT_DESCRIPTION
        defaultProductTransactionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productTransactionList where description equals to UPDATED_DESCRIPTION
        defaultProductTransactionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description not equals to DEFAULT_DESCRIPTION
        defaultProductTransactionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productTransactionList where description not equals to UPDATED_DESCRIPTION
        defaultProductTransactionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductTransactionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productTransactionList where description equals to UPDATED_DESCRIPTION
        defaultProductTransactionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description is not null
        defaultProductTransactionShouldBeFound("description.specified=true");

        // Get all the productTransactionList where description is null
        defaultProductTransactionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description contains DEFAULT_DESCRIPTION
        defaultProductTransactionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productTransactionList where description contains UPDATED_DESCRIPTION
        defaultProductTransactionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where description does not contain DEFAULT_DESCRIPTION
        defaultProductTransactionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productTransactionList where description does not contain UPDATED_DESCRIPTION
        defaultProductTransactionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultProductTransactionShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the productTransactionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultProductTransactionShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultProductTransactionShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the productTransactionList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultProductTransactionShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultProductTransactionShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the productTransactionList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultProductTransactionShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModified is not null
        defaultProductTransactionShouldBeFound("lastModified.specified=true");

        // Get all the productTransactionList where lastModified is null
        defaultProductTransactionShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultProductTransactionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productTransactionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productTransactionList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the productTransactionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy is not null
        defaultProductTransactionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the productTransactionList where lastModifiedBy is null
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultProductTransactionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productTransactionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultProductTransactionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productTransactionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultProductTransactionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductTransactionsByEcurityUserIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);
        SecurityUser ecurityUser;
        if (TestUtil.findAll(em, SecurityUser.class).isEmpty()) {
            ecurityUser = SecurityUserResourceIT.createEntity(em);
            em.persist(ecurityUser);
            em.flush();
        } else {
            ecurityUser = TestUtil.findAll(em, SecurityUser.class).get(0);
        }
        em.persist(ecurityUser);
        em.flush();
        productTransaction.setEcurityUser(ecurityUser);
        productTransactionRepository.saveAndFlush(productTransaction);
        Long ecurityUserId = ecurityUser.getId();

        // Get all the productTransactionList where ecurityUser equals to ecurityUserId
        defaultProductTransactionShouldBeFound("ecurityUserId.equals=" + ecurityUserId);

        // Get all the productTransactionList where ecurityUser equals to (ecurityUserId + 1)
        defaultProductTransactionShouldNotBeFound("ecurityUserId.equals=" + (ecurityUserId + 1));
    }

    @Test
    @Transactional
    void getAllProductTransactionsByWareHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);
        WareHouse wareHouse;
        if (TestUtil.findAll(em, WareHouse.class).isEmpty()) {
            wareHouse = WareHouseResourceIT.createEntity(em);
            em.persist(wareHouse);
            em.flush();
        } else {
            wareHouse = TestUtil.findAll(em, WareHouse.class).get(0);
        }
        em.persist(wareHouse);
        em.flush();
        productTransaction.setWareHouse(wareHouse);
        productTransactionRepository.saveAndFlush(productTransaction);
        Long wareHouseId = wareHouse.getId();

        // Get all the productTransactionList where wareHouse equals to wareHouseId
        defaultProductTransactionShouldBeFound("wareHouseId.equals=" + wareHouseId);

        // Get all the productTransactionList where wareHouse equals to (wareHouseId + 1)
        defaultProductTransactionShouldNotBeFound("wareHouseId.equals=" + (wareHouseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductTransactionShouldBeFound(String filter) throws Exception {
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtySold").value(hasItem(DEFAULT_QTY_SOLD.doubleValue())))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].lotNumber").value(hasItem(DEFAULT_LOT_NUMBER)))
            .andExpect(jsonPath("$.[*].expirydate").value(hasItem(DEFAULT_EXPIRYDATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].gstAmount").value(hasItem(DEFAULT_GST_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductTransactionShouldNotBeFound(String filter) throws Exception {
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductTransaction() throws Exception {
        // Get the productTransaction
        restProductTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductTransaction() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();

        // Update the productTransaction
        ProductTransaction updatedProductTransaction = productTransactionRepository.findById(productTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedProductTransaction are not directly saved in db
        em.detach(updatedProductTransaction);
        updatedProductTransaction
            .qtySold(UPDATED_QTY_SOLD)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .lotNumber(UPDATED_LOT_NUMBER)
            .expirydate(UPDATED_EXPIRYDATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .gstAmount(UPDATED_GST_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(updatedProductTransaction);

        restProductTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
        ProductTransaction testProductTransaction = productTransactionList.get(productTransactionList.size() - 1);
        assertThat(testProductTransaction.getQtySold()).isEqualTo(UPDATED_QTY_SOLD);
        assertThat(testProductTransaction.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testProductTransaction.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testProductTransaction.getExpirydate()).isEqualTo(UPDATED_EXPIRYDATE);
        assertThat(testProductTransaction.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testProductTransaction.getGstAmount()).isEqualTo(UPDATED_GST_AMOUNT);
        assertThat(testProductTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductTransaction.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testProductTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductTransactionWithPatch() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();

        // Update the productTransaction using partial update
        ProductTransaction partialUpdatedProductTransaction = new ProductTransaction();
        partialUpdatedProductTransaction.setId(productTransaction.getId());

        partialUpdatedProductTransaction
            .lotNumber(UPDATED_LOT_NUMBER)
            .expirydate(UPDATED_EXPIRYDATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restProductTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
        ProductTransaction testProductTransaction = productTransactionList.get(productTransactionList.size() - 1);
        assertThat(testProductTransaction.getQtySold()).isEqualTo(DEFAULT_QTY_SOLD);
        assertThat(testProductTransaction.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testProductTransaction.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testProductTransaction.getExpirydate()).isEqualTo(UPDATED_EXPIRYDATE);
        assertThat(testProductTransaction.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testProductTransaction.getGstAmount()).isEqualTo(DEFAULT_GST_AMOUNT);
        assertThat(testProductTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductTransaction.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testProductTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductTransactionWithPatch() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();

        // Update the productTransaction using partial update
        ProductTransaction partialUpdatedProductTransaction = new ProductTransaction();
        partialUpdatedProductTransaction.setId(productTransaction.getId());

        partialUpdatedProductTransaction
            .qtySold(UPDATED_QTY_SOLD)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .lotNumber(UPDATED_LOT_NUMBER)
            .expirydate(UPDATED_EXPIRYDATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .gstAmount(UPDATED_GST_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restProductTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
        ProductTransaction testProductTransaction = productTransactionList.get(productTransactionList.size() - 1);
        assertThat(testProductTransaction.getQtySold()).isEqualTo(UPDATED_QTY_SOLD);
        assertThat(testProductTransaction.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testProductTransaction.getLotNumber()).isEqualTo(UPDATED_LOT_NUMBER);
        assertThat(testProductTransaction.getExpirydate()).isEqualTo(UPDATED_EXPIRYDATE);
        assertThat(testProductTransaction.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testProductTransaction.getGstAmount()).isEqualTo(UPDATED_GST_AMOUNT);
        assertThat(testProductTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductTransaction.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testProductTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductTransaction() throws Exception {
        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();
        productTransaction.setId(count.incrementAndGet());

        // Create the ProductTransaction
        ProductTransactionDTO productTransactionDTO = productTransactionMapper.toDto(productTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductTransaction() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        int databaseSizeBeforeDelete = productTransactionRepository.findAll().size();

        // Delete the productTransaction
        restProductTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, productTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductTransaction> productTransactionList = productTransactionRepository.findAll();
        assertThat(productTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
