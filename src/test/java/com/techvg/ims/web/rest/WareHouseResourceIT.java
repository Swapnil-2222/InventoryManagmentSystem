package com.techvg.ims.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techvg.ims.IntegrationTest;
import com.techvg.ims.domain.ProductInventory;
import com.techvg.ims.domain.WareHouse;
import com.techvg.ims.repository.WareHouseRepository;
import com.techvg.ims.service.criteria.WareHouseCriteria;
import com.techvg.ims.service.dto.WareHouseDTO;
import com.techvg.ims.service.mapper.WareHouseMapper;
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
 * Integration tests for the {@link WareHouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WareHouseResourceIT {

    private static final String DEFAULT_WARE_HOUSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WARE_HOUSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PINCODE = 1;
    private static final Integer UPDATED_PINCODE = 2;
    private static final Integer SMALLER_PINCODE = 1 - 1;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_GST_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_GST_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ware-houses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WareHouseRepository wareHouseRepository;

    @Autowired
    private WareHouseMapper wareHouseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWareHouseMockMvc;

    private WareHouse wareHouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WareHouse createEntity(EntityManager em) {
        WareHouse wareHouse = new WareHouse()
            .wareHouseName(DEFAULT_WARE_HOUSE_NAME)
            .address(DEFAULT_ADDRESS)
            .pincode(DEFAULT_PINCODE)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .gstDetails(DEFAULT_GST_DETAILS)
            .managerName(DEFAULT_MANAGER_NAME)
            .managerEmail(DEFAULT_MANAGER_EMAIL)
            .managerContact(DEFAULT_MANAGER_CONTACT)
            .contact(DEFAULT_CONTACT)
            .isDeleted(DEFAULT_IS_DELETED)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return wareHouse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WareHouse createUpdatedEntity(EntityManager em) {
        WareHouse wareHouse = new WareHouse()
            .wareHouseName(UPDATED_WARE_HOUSE_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .gstDetails(UPDATED_GST_DETAILS)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL)
            .managerContact(UPDATED_MANAGER_CONTACT)
            .contact(UPDATED_CONTACT)
            .isDeleted(UPDATED_IS_DELETED)
            .isActive(UPDATED_IS_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return wareHouse;
    }

    @BeforeEach
    public void initTest() {
        wareHouse = createEntity(em);
    }

    @Test
    @Transactional
    void createWareHouse() throws Exception {
        int databaseSizeBeforeCreate = wareHouseRepository.findAll().size();
        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);
        restWareHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wareHouseDTO)))
            .andExpect(status().isCreated());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeCreate + 1);
        WareHouse testWareHouse = wareHouseList.get(wareHouseList.size() - 1);
        assertThat(testWareHouse.getWareHouseName()).isEqualTo(DEFAULT_WARE_HOUSE_NAME);
        assertThat(testWareHouse.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testWareHouse.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testWareHouse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testWareHouse.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testWareHouse.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testWareHouse.getGstDetails()).isEqualTo(DEFAULT_GST_DETAILS);
        assertThat(testWareHouse.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testWareHouse.getManagerEmail()).isEqualTo(DEFAULT_MANAGER_EMAIL);
        assertThat(testWareHouse.getManagerContact()).isEqualTo(DEFAULT_MANAGER_CONTACT);
        assertThat(testWareHouse.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testWareHouse.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testWareHouse.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testWareHouse.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testWareHouse.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createWareHouseWithExistingId() throws Exception {
        // Create the WareHouse with an existing ID
        wareHouse.setId(1L);
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        int databaseSizeBeforeCreate = wareHouseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWareHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wareHouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = wareHouseRepository.findAll().size();
        // set the field null
        wareHouse.setLastModified(null);

        // Create the WareHouse, which fails.
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        restWareHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wareHouseDTO)))
            .andExpect(status().isBadRequest());

        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = wareHouseRepository.findAll().size();
        // set the field null
        wareHouse.setLastModifiedBy(null);

        // Create the WareHouse, which fails.
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        restWareHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wareHouseDTO)))
            .andExpect(status().isBadRequest());

        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWareHouses() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wareHouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].wareHouseName").value(hasItem(DEFAULT_WARE_HOUSE_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].gstDetails").value(hasItem(DEFAULT_GST_DETAILS)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].managerEmail").value(hasItem(DEFAULT_MANAGER_EMAIL)))
            .andExpect(jsonPath("$.[*].managerContact").value(hasItem(DEFAULT_MANAGER_CONTACT)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getWareHouse() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get the wareHouse
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL_ID, wareHouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wareHouse.getId().intValue()))
            .andExpect(jsonPath("$.wareHouseName").value(DEFAULT_WARE_HOUSE_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.gstDetails").value(DEFAULT_GST_DETAILS))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME))
            .andExpect(jsonPath("$.managerEmail").value(DEFAULT_MANAGER_EMAIL))
            .andExpect(jsonPath("$.managerContact").value(DEFAULT_MANAGER_CONTACT))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getWareHousesByIdFiltering() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        Long id = wareHouse.getId();

        defaultWareHouseShouldBeFound("id.equals=" + id);
        defaultWareHouseShouldNotBeFound("id.notEquals=" + id);

        defaultWareHouseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWareHouseShouldNotBeFound("id.greaterThan=" + id);

        defaultWareHouseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWareHouseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName equals to DEFAULT_WARE_HOUSE_NAME
        defaultWareHouseShouldBeFound("wareHouseName.equals=" + DEFAULT_WARE_HOUSE_NAME);

        // Get all the wareHouseList where wareHouseName equals to UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldNotBeFound("wareHouseName.equals=" + UPDATED_WARE_HOUSE_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName not equals to DEFAULT_WARE_HOUSE_NAME
        defaultWareHouseShouldNotBeFound("wareHouseName.notEquals=" + DEFAULT_WARE_HOUSE_NAME);

        // Get all the wareHouseList where wareHouseName not equals to UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldBeFound("wareHouseName.notEquals=" + UPDATED_WARE_HOUSE_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName in DEFAULT_WARE_HOUSE_NAME or UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldBeFound("wareHouseName.in=" + DEFAULT_WARE_HOUSE_NAME + "," + UPDATED_WARE_HOUSE_NAME);

        // Get all the wareHouseList where wareHouseName equals to UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldNotBeFound("wareHouseName.in=" + UPDATED_WARE_HOUSE_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName is not null
        defaultWareHouseShouldBeFound("wareHouseName.specified=true");

        // Get all the wareHouseList where wareHouseName is null
        defaultWareHouseShouldNotBeFound("wareHouseName.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName contains DEFAULT_WARE_HOUSE_NAME
        defaultWareHouseShouldBeFound("wareHouseName.contains=" + DEFAULT_WARE_HOUSE_NAME);

        // Get all the wareHouseList where wareHouseName contains UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldNotBeFound("wareHouseName.contains=" + UPDATED_WARE_HOUSE_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByWareHouseNameNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where wareHouseName does not contain DEFAULT_WARE_HOUSE_NAME
        defaultWareHouseShouldNotBeFound("wareHouseName.doesNotContain=" + DEFAULT_WARE_HOUSE_NAME);

        // Get all the wareHouseList where wareHouseName does not contain UPDATED_WARE_HOUSE_NAME
        defaultWareHouseShouldBeFound("wareHouseName.doesNotContain=" + UPDATED_WARE_HOUSE_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address equals to DEFAULT_ADDRESS
        defaultWareHouseShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the wareHouseList where address equals to UPDATED_ADDRESS
        defaultWareHouseShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address not equals to DEFAULT_ADDRESS
        defaultWareHouseShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the wareHouseList where address not equals to UPDATED_ADDRESS
        defaultWareHouseShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultWareHouseShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the wareHouseList where address equals to UPDATED_ADDRESS
        defaultWareHouseShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address is not null
        defaultWareHouseShouldBeFound("address.specified=true");

        // Get all the wareHouseList where address is null
        defaultWareHouseShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address contains DEFAULT_ADDRESS
        defaultWareHouseShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the wareHouseList where address contains UPDATED_ADDRESS
        defaultWareHouseShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWareHousesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where address does not contain DEFAULT_ADDRESS
        defaultWareHouseShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the wareHouseList where address does not contain UPDATED_ADDRESS
        defaultWareHouseShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode equals to DEFAULT_PINCODE
        defaultWareHouseShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode equals to UPDATED_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode not equals to DEFAULT_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.notEquals=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode not equals to UPDATED_PINCODE
        defaultWareHouseShouldBeFound("pincode.notEquals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultWareHouseShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the wareHouseList where pincode equals to UPDATED_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode is not null
        defaultWareHouseShouldBeFound("pincode.specified=true");

        // Get all the wareHouseList where pincode is null
        defaultWareHouseShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode is greater than or equal to DEFAULT_PINCODE
        defaultWareHouseShouldBeFound("pincode.greaterThanOrEqual=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode is greater than or equal to UPDATED_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.greaterThanOrEqual=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode is less than or equal to DEFAULT_PINCODE
        defaultWareHouseShouldBeFound("pincode.lessThanOrEqual=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode is less than or equal to SMALLER_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.lessThanOrEqual=" + SMALLER_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsLessThanSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode is less than DEFAULT_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.lessThan=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode is less than UPDATED_PINCODE
        defaultWareHouseShouldBeFound("pincode.lessThan=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByPincodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where pincode is greater than DEFAULT_PINCODE
        defaultWareHouseShouldNotBeFound("pincode.greaterThan=" + DEFAULT_PINCODE);

        // Get all the wareHouseList where pincode is greater than SMALLER_PINCODE
        defaultWareHouseShouldBeFound("pincode.greaterThan=" + SMALLER_PINCODE);
    }

    @Test
    @Transactional
    void getAllWareHousesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city equals to DEFAULT_CITY
        defaultWareHouseShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the wareHouseList where city equals to UPDATED_CITY
        defaultWareHouseShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city not equals to DEFAULT_CITY
        defaultWareHouseShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the wareHouseList where city not equals to UPDATED_CITY
        defaultWareHouseShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city in DEFAULT_CITY or UPDATED_CITY
        defaultWareHouseShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the wareHouseList where city equals to UPDATED_CITY
        defaultWareHouseShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city is not null
        defaultWareHouseShouldBeFound("city.specified=true");

        // Get all the wareHouseList where city is null
        defaultWareHouseShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByCityContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city contains DEFAULT_CITY
        defaultWareHouseShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the wareHouseList where city contains UPDATED_CITY
        defaultWareHouseShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where city does not contain DEFAULT_CITY
        defaultWareHouseShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the wareHouseList where city does not contain UPDATED_CITY
        defaultWareHouseShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWareHousesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state equals to DEFAULT_STATE
        defaultWareHouseShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the wareHouseList where state equals to UPDATED_STATE
        defaultWareHouseShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllWareHousesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state not equals to DEFAULT_STATE
        defaultWareHouseShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the wareHouseList where state not equals to UPDATED_STATE
        defaultWareHouseShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllWareHousesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state in DEFAULT_STATE or UPDATED_STATE
        defaultWareHouseShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the wareHouseList where state equals to UPDATED_STATE
        defaultWareHouseShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllWareHousesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state is not null
        defaultWareHouseShouldBeFound("state.specified=true");

        // Get all the wareHouseList where state is null
        defaultWareHouseShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByStateContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state contains DEFAULT_STATE
        defaultWareHouseShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the wareHouseList where state contains UPDATED_STATE
        defaultWareHouseShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllWareHousesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where state does not contain DEFAULT_STATE
        defaultWareHouseShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the wareHouseList where state does not contain UPDATED_STATE
        defaultWareHouseShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country equals to DEFAULT_COUNTRY
        defaultWareHouseShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the wareHouseList where country equals to UPDATED_COUNTRY
        defaultWareHouseShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country not equals to DEFAULT_COUNTRY
        defaultWareHouseShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the wareHouseList where country not equals to UPDATED_COUNTRY
        defaultWareHouseShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultWareHouseShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the wareHouseList where country equals to UPDATED_COUNTRY
        defaultWareHouseShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country is not null
        defaultWareHouseShouldBeFound("country.specified=true");

        // Get all the wareHouseList where country is null
        defaultWareHouseShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country contains DEFAULT_COUNTRY
        defaultWareHouseShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the wareHouseList where country contains UPDATED_COUNTRY
        defaultWareHouseShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWareHousesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where country does not contain DEFAULT_COUNTRY
        defaultWareHouseShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the wareHouseList where country does not contain UPDATED_COUNTRY
        defaultWareHouseShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails equals to DEFAULT_GST_DETAILS
        defaultWareHouseShouldBeFound("gstDetails.equals=" + DEFAULT_GST_DETAILS);

        // Get all the wareHouseList where gstDetails equals to UPDATED_GST_DETAILS
        defaultWareHouseShouldNotBeFound("gstDetails.equals=" + UPDATED_GST_DETAILS);
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails not equals to DEFAULT_GST_DETAILS
        defaultWareHouseShouldNotBeFound("gstDetails.notEquals=" + DEFAULT_GST_DETAILS);

        // Get all the wareHouseList where gstDetails not equals to UPDATED_GST_DETAILS
        defaultWareHouseShouldBeFound("gstDetails.notEquals=" + UPDATED_GST_DETAILS);
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails in DEFAULT_GST_DETAILS or UPDATED_GST_DETAILS
        defaultWareHouseShouldBeFound("gstDetails.in=" + DEFAULT_GST_DETAILS + "," + UPDATED_GST_DETAILS);

        // Get all the wareHouseList where gstDetails equals to UPDATED_GST_DETAILS
        defaultWareHouseShouldNotBeFound("gstDetails.in=" + UPDATED_GST_DETAILS);
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails is not null
        defaultWareHouseShouldBeFound("gstDetails.specified=true");

        // Get all the wareHouseList where gstDetails is null
        defaultWareHouseShouldNotBeFound("gstDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails contains DEFAULT_GST_DETAILS
        defaultWareHouseShouldBeFound("gstDetails.contains=" + DEFAULT_GST_DETAILS);

        // Get all the wareHouseList where gstDetails contains UPDATED_GST_DETAILS
        defaultWareHouseShouldNotBeFound("gstDetails.contains=" + UPDATED_GST_DETAILS);
    }

    @Test
    @Transactional
    void getAllWareHousesByGstDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where gstDetails does not contain DEFAULT_GST_DETAILS
        defaultWareHouseShouldNotBeFound("gstDetails.doesNotContain=" + DEFAULT_GST_DETAILS);

        // Get all the wareHouseList where gstDetails does not contain UPDATED_GST_DETAILS
        defaultWareHouseShouldBeFound("gstDetails.doesNotContain=" + UPDATED_GST_DETAILS);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName equals to DEFAULT_MANAGER_NAME
        defaultWareHouseShouldBeFound("managerName.equals=" + DEFAULT_MANAGER_NAME);

        // Get all the wareHouseList where managerName equals to UPDATED_MANAGER_NAME
        defaultWareHouseShouldNotBeFound("managerName.equals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName not equals to DEFAULT_MANAGER_NAME
        defaultWareHouseShouldNotBeFound("managerName.notEquals=" + DEFAULT_MANAGER_NAME);

        // Get all the wareHouseList where managerName not equals to UPDATED_MANAGER_NAME
        defaultWareHouseShouldBeFound("managerName.notEquals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName in DEFAULT_MANAGER_NAME or UPDATED_MANAGER_NAME
        defaultWareHouseShouldBeFound("managerName.in=" + DEFAULT_MANAGER_NAME + "," + UPDATED_MANAGER_NAME);

        // Get all the wareHouseList where managerName equals to UPDATED_MANAGER_NAME
        defaultWareHouseShouldNotBeFound("managerName.in=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName is not null
        defaultWareHouseShouldBeFound("managerName.specified=true");

        // Get all the wareHouseList where managerName is null
        defaultWareHouseShouldNotBeFound("managerName.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName contains DEFAULT_MANAGER_NAME
        defaultWareHouseShouldBeFound("managerName.contains=" + DEFAULT_MANAGER_NAME);

        // Get all the wareHouseList where managerName contains UPDATED_MANAGER_NAME
        defaultWareHouseShouldNotBeFound("managerName.contains=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerNameNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerName does not contain DEFAULT_MANAGER_NAME
        defaultWareHouseShouldNotBeFound("managerName.doesNotContain=" + DEFAULT_MANAGER_NAME);

        // Get all the wareHouseList where managerName does not contain UPDATED_MANAGER_NAME
        defaultWareHouseShouldBeFound("managerName.doesNotContain=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail equals to DEFAULT_MANAGER_EMAIL
        defaultWareHouseShouldBeFound("managerEmail.equals=" + DEFAULT_MANAGER_EMAIL);

        // Get all the wareHouseList where managerEmail equals to UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldNotBeFound("managerEmail.equals=" + UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail not equals to DEFAULT_MANAGER_EMAIL
        defaultWareHouseShouldNotBeFound("managerEmail.notEquals=" + DEFAULT_MANAGER_EMAIL);

        // Get all the wareHouseList where managerEmail not equals to UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldBeFound("managerEmail.notEquals=" + UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail in DEFAULT_MANAGER_EMAIL or UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldBeFound("managerEmail.in=" + DEFAULT_MANAGER_EMAIL + "," + UPDATED_MANAGER_EMAIL);

        // Get all the wareHouseList where managerEmail equals to UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldNotBeFound("managerEmail.in=" + UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail is not null
        defaultWareHouseShouldBeFound("managerEmail.specified=true");

        // Get all the wareHouseList where managerEmail is null
        defaultWareHouseShouldNotBeFound("managerEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail contains DEFAULT_MANAGER_EMAIL
        defaultWareHouseShouldBeFound("managerEmail.contains=" + DEFAULT_MANAGER_EMAIL);

        // Get all the wareHouseList where managerEmail contains UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldNotBeFound("managerEmail.contains=" + UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerEmailNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerEmail does not contain DEFAULT_MANAGER_EMAIL
        defaultWareHouseShouldNotBeFound("managerEmail.doesNotContain=" + DEFAULT_MANAGER_EMAIL);

        // Get all the wareHouseList where managerEmail does not contain UPDATED_MANAGER_EMAIL
        defaultWareHouseShouldBeFound("managerEmail.doesNotContain=" + UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact equals to DEFAULT_MANAGER_CONTACT
        defaultWareHouseShouldBeFound("managerContact.equals=" + DEFAULT_MANAGER_CONTACT);

        // Get all the wareHouseList where managerContact equals to UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldNotBeFound("managerContact.equals=" + UPDATED_MANAGER_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact not equals to DEFAULT_MANAGER_CONTACT
        defaultWareHouseShouldNotBeFound("managerContact.notEquals=" + DEFAULT_MANAGER_CONTACT);

        // Get all the wareHouseList where managerContact not equals to UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldBeFound("managerContact.notEquals=" + UPDATED_MANAGER_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact in DEFAULT_MANAGER_CONTACT or UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldBeFound("managerContact.in=" + DEFAULT_MANAGER_CONTACT + "," + UPDATED_MANAGER_CONTACT);

        // Get all the wareHouseList where managerContact equals to UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldNotBeFound("managerContact.in=" + UPDATED_MANAGER_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact is not null
        defaultWareHouseShouldBeFound("managerContact.specified=true");

        // Get all the wareHouseList where managerContact is null
        defaultWareHouseShouldNotBeFound("managerContact.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact contains DEFAULT_MANAGER_CONTACT
        defaultWareHouseShouldBeFound("managerContact.contains=" + DEFAULT_MANAGER_CONTACT);

        // Get all the wareHouseList where managerContact contains UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldNotBeFound("managerContact.contains=" + UPDATED_MANAGER_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByManagerContactNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where managerContact does not contain DEFAULT_MANAGER_CONTACT
        defaultWareHouseShouldNotBeFound("managerContact.doesNotContain=" + DEFAULT_MANAGER_CONTACT);

        // Get all the wareHouseList where managerContact does not contain UPDATED_MANAGER_CONTACT
        defaultWareHouseShouldBeFound("managerContact.doesNotContain=" + UPDATED_MANAGER_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact equals to DEFAULT_CONTACT
        defaultWareHouseShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the wareHouseList where contact equals to UPDATED_CONTACT
        defaultWareHouseShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByContactIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact not equals to DEFAULT_CONTACT
        defaultWareHouseShouldNotBeFound("contact.notEquals=" + DEFAULT_CONTACT);

        // Get all the wareHouseList where contact not equals to UPDATED_CONTACT
        defaultWareHouseShouldBeFound("contact.notEquals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByContactIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultWareHouseShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the wareHouseList where contact equals to UPDATED_CONTACT
        defaultWareHouseShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact is not null
        defaultWareHouseShouldBeFound("contact.specified=true");

        // Get all the wareHouseList where contact is null
        defaultWareHouseShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByContactContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact contains DEFAULT_CONTACT
        defaultWareHouseShouldBeFound("contact.contains=" + DEFAULT_CONTACT);

        // Get all the wareHouseList where contact contains UPDATED_CONTACT
        defaultWareHouseShouldNotBeFound("contact.contains=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByContactNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where contact does not contain DEFAULT_CONTACT
        defaultWareHouseShouldNotBeFound("contact.doesNotContain=" + DEFAULT_CONTACT);

        // Get all the wareHouseList where contact does not contain UPDATED_CONTACT
        defaultWareHouseShouldBeFound("contact.doesNotContain=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isDeleted equals to DEFAULT_IS_DELETED
        defaultWareHouseShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the wareHouseList where isDeleted equals to UPDATED_IS_DELETED
        defaultWareHouseShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsDeletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isDeleted not equals to DEFAULT_IS_DELETED
        defaultWareHouseShouldNotBeFound("isDeleted.notEquals=" + DEFAULT_IS_DELETED);

        // Get all the wareHouseList where isDeleted not equals to UPDATED_IS_DELETED
        defaultWareHouseShouldBeFound("isDeleted.notEquals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultWareHouseShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the wareHouseList where isDeleted equals to UPDATED_IS_DELETED
        defaultWareHouseShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isDeleted is not null
        defaultWareHouseShouldBeFound("isDeleted.specified=true");

        // Get all the wareHouseList where isDeleted is null
        defaultWareHouseShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isActive equals to DEFAULT_IS_ACTIVE
        defaultWareHouseShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the wareHouseList where isActive equals to UPDATED_IS_ACTIVE
        defaultWareHouseShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultWareHouseShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the wareHouseList where isActive not equals to UPDATED_IS_ACTIVE
        defaultWareHouseShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultWareHouseShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the wareHouseList where isActive equals to UPDATED_IS_ACTIVE
        defaultWareHouseShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWareHousesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where isActive is not null
        defaultWareHouseShouldBeFound("isActive.specified=true");

        // Get all the wareHouseList where isActive is null
        defaultWareHouseShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultWareHouseShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the wareHouseList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultWareHouseShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultWareHouseShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the wareHouseList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultWareHouseShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultWareHouseShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the wareHouseList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultWareHouseShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModified is not null
        defaultWareHouseShouldBeFound("lastModified.specified=true");

        // Get all the wareHouseList where lastModified is null
        defaultWareHouseShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultWareHouseShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wareHouseList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultWareHouseShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wareHouseList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the wareHouseList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy is not null
        defaultWareHouseShouldBeFound("lastModifiedBy.specified=true");

        // Get all the wareHouseList where lastModifiedBy is null
        defaultWareHouseShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultWareHouseShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wareHouseList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWareHousesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        // Get all the wareHouseList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultWareHouseShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wareHouseList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultWareHouseShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWareHousesByProductInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);
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
        wareHouse.addProductInventory(productInventory);
        wareHouseRepository.saveAndFlush(wareHouse);
        Long productInventoryId = productInventory.getId();

        // Get all the wareHouseList where productInventory equals to productInventoryId
        defaultWareHouseShouldBeFound("productInventoryId.equals=" + productInventoryId);

        // Get all the wareHouseList where productInventory equals to (productInventoryId + 1)
        defaultWareHouseShouldNotBeFound("productInventoryId.equals=" + (productInventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWareHouseShouldBeFound(String filter) throws Exception {
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wareHouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].wareHouseName").value(hasItem(DEFAULT_WARE_HOUSE_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].gstDetails").value(hasItem(DEFAULT_GST_DETAILS)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].managerEmail").value(hasItem(DEFAULT_MANAGER_EMAIL)))
            .andExpect(jsonPath("$.[*].managerContact").value(hasItem(DEFAULT_MANAGER_CONTACT)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWareHouseShouldNotBeFound(String filter) throws Exception {
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWareHouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWareHouse() throws Exception {
        // Get the wareHouse
        restWareHouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWareHouse() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();

        // Update the wareHouse
        WareHouse updatedWareHouse = wareHouseRepository.findById(wareHouse.getId()).get();
        // Disconnect from session so that the updates on updatedWareHouse are not directly saved in db
        em.detach(updatedWareHouse);
        updatedWareHouse
            .wareHouseName(UPDATED_WARE_HOUSE_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .gstDetails(UPDATED_GST_DETAILS)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL)
            .managerContact(UPDATED_MANAGER_CONTACT)
            .contact(UPDATED_CONTACT)
            .isDeleted(UPDATED_IS_DELETED)
            .isActive(UPDATED_IS_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(updatedWareHouse);

        restWareHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wareHouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isOk());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
        WareHouse testWareHouse = wareHouseList.get(wareHouseList.size() - 1);
        assertThat(testWareHouse.getWareHouseName()).isEqualTo(UPDATED_WARE_HOUSE_NAME);
        assertThat(testWareHouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWareHouse.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testWareHouse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testWareHouse.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWareHouse.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testWareHouse.getGstDetails()).isEqualTo(UPDATED_GST_DETAILS);
        assertThat(testWareHouse.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testWareHouse.getManagerEmail()).isEqualTo(UPDATED_MANAGER_EMAIL);
        assertThat(testWareHouse.getManagerContact()).isEqualTo(UPDATED_MANAGER_CONTACT);
        assertThat(testWareHouse.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testWareHouse.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testWareHouse.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWareHouse.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testWareHouse.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wareHouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wareHouseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWareHouseWithPatch() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();

        // Update the wareHouse using partial update
        WareHouse partialUpdatedWareHouse = new WareHouse();
        partialUpdatedWareHouse.setId(wareHouse.getId());

        partialUpdatedWareHouse
            .wareHouseName(UPDATED_WARE_HOUSE_NAME)
            .gstDetails(UPDATED_GST_DETAILS)
            .managerName(UPDATED_MANAGER_NAME)
            .managerContact(UPDATED_MANAGER_CONTACT)
            .isActive(UPDATED_IS_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restWareHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWareHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWareHouse))
            )
            .andExpect(status().isOk());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
        WareHouse testWareHouse = wareHouseList.get(wareHouseList.size() - 1);
        assertThat(testWareHouse.getWareHouseName()).isEqualTo(UPDATED_WARE_HOUSE_NAME);
        assertThat(testWareHouse.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testWareHouse.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testWareHouse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testWareHouse.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testWareHouse.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testWareHouse.getGstDetails()).isEqualTo(UPDATED_GST_DETAILS);
        assertThat(testWareHouse.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testWareHouse.getManagerEmail()).isEqualTo(DEFAULT_MANAGER_EMAIL);
        assertThat(testWareHouse.getManagerContact()).isEqualTo(UPDATED_MANAGER_CONTACT);
        assertThat(testWareHouse.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testWareHouse.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testWareHouse.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWareHouse.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testWareHouse.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateWareHouseWithPatch() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();

        // Update the wareHouse using partial update
        WareHouse partialUpdatedWareHouse = new WareHouse();
        partialUpdatedWareHouse.setId(wareHouse.getId());

        partialUpdatedWareHouse
            .wareHouseName(UPDATED_WARE_HOUSE_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .gstDetails(UPDATED_GST_DETAILS)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL)
            .managerContact(UPDATED_MANAGER_CONTACT)
            .contact(UPDATED_CONTACT)
            .isDeleted(UPDATED_IS_DELETED)
            .isActive(UPDATED_IS_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restWareHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWareHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWareHouse))
            )
            .andExpect(status().isOk());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
        WareHouse testWareHouse = wareHouseList.get(wareHouseList.size() - 1);
        assertThat(testWareHouse.getWareHouseName()).isEqualTo(UPDATED_WARE_HOUSE_NAME);
        assertThat(testWareHouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWareHouse.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testWareHouse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testWareHouse.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWareHouse.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testWareHouse.getGstDetails()).isEqualTo(UPDATED_GST_DETAILS);
        assertThat(testWareHouse.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testWareHouse.getManagerEmail()).isEqualTo(UPDATED_MANAGER_EMAIL);
        assertThat(testWareHouse.getManagerContact()).isEqualTo(UPDATED_MANAGER_CONTACT);
        assertThat(testWareHouse.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testWareHouse.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testWareHouse.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWareHouse.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testWareHouse.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wareHouseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWareHouse() throws Exception {
        int databaseSizeBeforeUpdate = wareHouseRepository.findAll().size();
        wareHouse.setId(count.incrementAndGet());

        // Create the WareHouse
        WareHouseDTO wareHouseDTO = wareHouseMapper.toDto(wareHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWareHouseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wareHouseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WareHouse in the database
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWareHouse() throws Exception {
        // Initialize the database
        wareHouseRepository.saveAndFlush(wareHouse);

        int databaseSizeBeforeDelete = wareHouseRepository.findAll().size();

        // Delete the wareHouse
        restWareHouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, wareHouse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WareHouse> wareHouseList = wareHouseRepository.findAll();
        assertThat(wareHouseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
