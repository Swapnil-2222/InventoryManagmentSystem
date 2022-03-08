package com.techvg.ims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductTransaction.
 */
@Entity
@Table(name = "product_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qty_sold")
    private Double qtySold;

    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "expirydate")
    private Instant expirydate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "gst_amount")
    private Double gstAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "securityPermissions", "securityRoles", "securityUsers", "productInventories" }, allowSetters = true)
    private SecurityUser ecurityUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productInventories", "productInventories" }, allowSetters = true)
    private WareHouse wareHouse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQtySold() {
        return this.qtySold;
    }

    public ProductTransaction qtySold(Double qtySold) {
        this.setQtySold(qtySold);
        return this;
    }

    public void setQtySold(Double qtySold) {
        this.qtySold = qtySold;
    }

    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    public ProductTransaction pricePerUnit(Double pricePerUnit) {
        this.setPricePerUnit(pricePerUnit);
        return this;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getLotNumber() {
        return this.lotNumber;
    }

    public ProductTransaction lotNumber(String lotNumber) {
        this.setLotNumber(lotNumber);
        return this;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Instant getExpirydate() {
        return this.expirydate;
    }

    public ProductTransaction expirydate(Instant expirydate) {
        this.setExpirydate(expirydate);
        return this;
    }

    public void setExpirydate(Instant expirydate) {
        this.expirydate = expirydate;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public ProductTransaction totalAmount(Double totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getGstAmount() {
        return this.gstAmount;
    }

    public ProductTransaction gstAmount(Double gstAmount) {
        this.setGstAmount(gstAmount);
        return this;
    }

    public void setGstAmount(Double gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductTransaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public ProductTransaction lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ProductTransaction lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public SecurityUser getEcurityUser() {
        return this.ecurityUser;
    }

    public void setEcurityUser(SecurityUser securityUser) {
        this.ecurityUser = securityUser;
    }

    public ProductTransaction ecurityUser(SecurityUser securityUser) {
        this.setEcurityUser(securityUser);
        return this;
    }

    public WareHouse getWareHouse() {
        return this.wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    public ProductTransaction wareHouse(WareHouse wareHouse) {
        this.setWareHouse(wareHouse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductTransaction)) {
            return false;
        }
        return id != null && id.equals(((ProductTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductTransaction{" +
            "id=" + getId() +
            ", qtySold=" + getQtySold() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", lotNumber='" + getLotNumber() + "'" +
            ", expirydate='" + getExpirydate() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", gstAmount=" + getGstAmount() +
            ", description='" + getDescription() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
