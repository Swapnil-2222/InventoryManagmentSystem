package com.techvg.ims.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.techvg.ims.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseOrderDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderDetailsDTO.class);
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO1 = new PurchaseOrderDetailsDTO();
        purchaseOrderDetailsDTO1.setId(1L);
        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO2 = new PurchaseOrderDetailsDTO();
        assertThat(purchaseOrderDetailsDTO1).isNotEqualTo(purchaseOrderDetailsDTO2);
        purchaseOrderDetailsDTO2.setId(purchaseOrderDetailsDTO1.getId());
        assertThat(purchaseOrderDetailsDTO1).isEqualTo(purchaseOrderDetailsDTO2);
        purchaseOrderDetailsDTO2.setId(2L);
        assertThat(purchaseOrderDetailsDTO1).isNotEqualTo(purchaseOrderDetailsDTO2);
        purchaseOrderDetailsDTO1.setId(null);
        assertThat(purchaseOrderDetailsDTO1).isNotEqualTo(purchaseOrderDetailsDTO2);
    }
}
