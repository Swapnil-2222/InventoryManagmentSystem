package com.techvg.ims.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.techvg.ims.domain.PurchaseOrderDetails} entity.
 */
public class PurchaseOrderDetailsDTO implements Serializable {

    private Long id;

    private Double qtyordered;

    private Integer gstTaxPercentage;

    private Double pricePerUnit;

    private Double totalPrice;

    private Double discount;

    @NotNull
    private Instant lastModified;

    @NotNull
    private String lastModifiedBy;

    private String freeField1;

    private String freeField2;

    private PurchaseOrderDTO purchaseOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQtyordered() {
        return qtyordered;
    }

    public void setQtyordered(Double qtyordered) {
        this.qtyordered = qtyordered;
    }

    public Integer getGstTaxPercentage() {
        return gstTaxPercentage;
    }

    public void setGstTaxPercentage(Integer gstTaxPercentage) {
        this.gstTaxPercentage = gstTaxPercentage;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFreeField1() {
        return freeField1;
    }

    public void setFreeField1(String freeField1) {
        this.freeField1 = freeField1;
    }

    public String getFreeField2() {
        return freeField2;
    }

    public void setFreeField2(String freeField2) {
        this.freeField2 = freeField2;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderDetailsDTO)) {
            return false;
        }

        PurchaseOrderDetailsDTO purchaseOrderDetailsDTO = (PurchaseOrderDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseOrderDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderDetailsDTO{" +
            "id=" + getId() +
            ", qtyordered=" + getQtyordered() +
            ", gstTaxPercentage=" + getGstTaxPercentage() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalPrice=" + getTotalPrice() +
            ", discount=" + getDiscount() +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", freeField1='" + getFreeField1() + "'" +
            ", freeField2='" + getFreeField2() + "'" +
            ", purchaseOrder=" + getPurchaseOrder() +
            "}";
    }
}
