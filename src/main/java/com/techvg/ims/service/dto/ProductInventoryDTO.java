package com.techvg.ims.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.techvg.ims.domain.ProductInventory} entity.
 */
public class ProductInventoryDTO implements Serializable {

    private Long id;

    private Instant inwardOutwardDate;

    private String inwardQty;

    private String outwardQty;

    private String totalQuanity;

    private Long pricePerUnit;

    private String lotNo;

    private Instant expiryDate;

    private String inventoryTypeId;

    private String freeField1;

    private String freeField2;

    private Instant lastModified;

    private String lastModifiedBy;

    private Boolean isDeleted;

    private Boolean isActive;

    private ProductDTO product;

    private PurchaseOrderDTO purchaseOrder;

    private ProductTransactionDTO productTransaction;

    private Set<WareHouseDTO> wareHouses = new HashSet<>();

    private Set<SecurityUserDTO> securityUsers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInwardOutwardDate() {
        return inwardOutwardDate;
    }

    public void setInwardOutwardDate(Instant inwardOutwardDate) {
        this.inwardOutwardDate = inwardOutwardDate;
    }

    public String getInwardQty() {
        return inwardQty;
    }

    public void setInwardQty(String inwardQty) {
        this.inwardQty = inwardQty;
    }

    public String getOutwardQty() {
        return outwardQty;
    }

    public void setOutwardQty(String outwardQty) {
        this.outwardQty = outwardQty;
    }

    public String getTotalQuanity() {
        return totalQuanity;
    }

    public void setTotalQuanity(String totalQuanity) {
        this.totalQuanity = totalQuanity;
    }

    public Long getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Long pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getInventoryTypeId() {
        return inventoryTypeId;
    }

    public void setInventoryTypeId(String inventoryTypeId) {
        this.inventoryTypeId = inventoryTypeId;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public ProductTransactionDTO getProductTransaction() {
        return productTransaction;
    }

    public void setProductTransaction(ProductTransactionDTO productTransaction) {
        this.productTransaction = productTransaction;
    }

    public Set<WareHouseDTO> getWareHouses() {
        return wareHouses;
    }

    public void setWareHouses(Set<WareHouseDTO> wareHouses) {
        this.wareHouses = wareHouses;
    }

    public Set<SecurityUserDTO> getSecurityUsers() {
        return securityUsers;
    }

    public void setSecurityUsers(Set<SecurityUserDTO> securityUsers) {
        this.securityUsers = securityUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInventoryDTO)) {
            return false;
        }

        ProductInventoryDTO productInventoryDTO = (ProductInventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productInventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventoryDTO{" +
            "id=" + getId() +
            ", inwardOutwardDate='" + getInwardOutwardDate() + "'" +
            ", inwardQty='" + getInwardQty() + "'" +
            ", outwardQty='" + getOutwardQty() + "'" +
            ", totalQuanity='" + getTotalQuanity() + "'" +
            ", pricePerUnit=" + getPricePerUnit() +
            ", lotNo='" + getLotNo() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", inventoryTypeId='" + getInventoryTypeId() + "'" +
            ", freeField1='" + getFreeField1() + "'" +
            ", freeField2='" + getFreeField2() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", product=" + getProduct() +
            ", purchaseOrder=" + getPurchaseOrder() +
            ", productTransaction=" + getProductTransaction() +
            ", wareHouses=" + getWareHouses() +
            ", securityUsers=" + getSecurityUsers() +
            "}";
    }
}
