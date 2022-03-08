package com.techvg.ims.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOrderDetailsMapperTest {

    private PurchaseOrderDetailsMapper purchaseOrderDetailsMapper;

    @BeforeEach
    public void setUp() {
        purchaseOrderDetailsMapper = new PurchaseOrderDetailsMapperImpl();
    }
}
