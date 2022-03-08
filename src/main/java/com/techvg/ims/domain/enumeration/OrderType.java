package com.techvg.ims.domain.enumeration;

/**
 * The OrderType enumeration.
 */
public enum OrderType {
    PURCHASE_ORDER("PurchaseOrder"),
    PRODUCT_QUATATION("ProductQuatation");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
