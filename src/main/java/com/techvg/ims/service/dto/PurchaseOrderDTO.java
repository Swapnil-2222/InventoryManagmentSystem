package com.techvg.ims.service.dto;

import com.techvg.ims.domain.enumeration.OrderType;
import com.techvg.ims.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.techvg.ims.domain.PurchaseOrder} entity.
 */
public class PurchaseOrderDTO implements Serializable {

    private Long id;

    private Double totalPOAmount;

    private Double totalGSTAmount;

    private Instant expectedDeliveryDate;

    private Instant poDate;

    private OrderType orderType;

    private Status orderStatus;

    private String clientName;

    private String clientMobile;

    private String clientEmail;

    private String termsAndCondition;

    private String notes;

    @NotNull
    private Instant lastModified;

    @NotNull
    private String lastModifiedBy;

    private String freeField1;

    private String freeField2;

    private SecurityUserDTO securityUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPOAmount() {
        return totalPOAmount;
    }

    public void setTotalPOAmount(Double totalPOAmount) {
        this.totalPOAmount = totalPOAmount;
    }

    public Double getTotalGSTAmount() {
        return totalGSTAmount;
    }

    public void setTotalGSTAmount(Double totalGSTAmount) {
        this.totalGSTAmount = totalGSTAmount;
    }

    public Instant getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Instant getPoDate() {
        return poDate;
    }

    public void setPoDate(Instant poDate) {
        this.poDate = poDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public SecurityUserDTO getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUserDTO securityUser) {
        this.securityUser = securityUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderDTO)) {
            return false;
        }

        PurchaseOrderDTO purchaseOrderDTO = (PurchaseOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderDTO{" +
            "id=" + getId() +
            ", totalPOAmount=" + getTotalPOAmount() +
            ", totalGSTAmount=" + getTotalGSTAmount() +
            ", expectedDeliveryDate='" + getExpectedDeliveryDate() + "'" +
            ", poDate='" + getPoDate() + "'" +
            ", orderType='" + getOrderType() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", clientName='" + getClientName() + "'" +
            ", clientMobile='" + getClientMobile() + "'" +
            ", clientEmail='" + getClientEmail() + "'" +
            ", termsAndCondition='" + getTermsAndCondition() + "'" +
            ", notes='" + getNotes() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", freeField1='" + getFreeField1() + "'" +
            ", freeField2='" + getFreeField2() + "'" +
            ", securityUser=" + getSecurityUser() +
            "}";
    }
}
