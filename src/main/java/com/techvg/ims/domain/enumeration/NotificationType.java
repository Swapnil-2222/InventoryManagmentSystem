package com.techvg.ims.domain.enumeration;

/**
 * The NotificationType enumeration.
 */
public enum NotificationType {
    APPROVAL("Apporoval"),
    REQUEST("Product"),
    ALERT("Alert"),
    TRANSFER("Transfer");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
