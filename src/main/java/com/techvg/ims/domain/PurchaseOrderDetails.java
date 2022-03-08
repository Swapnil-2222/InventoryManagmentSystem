package com.techvg.ims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrderDetails.
 */
@Entity
@Table(name = "purchase_order_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PurchaseOrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qtyordered")
    private Double qtyordered;

    @Column(name = "gst_tax_percentage")
    private Integer gstTaxPercentage;

    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "discount")
    private Double discount;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @Column(name = "free_field_1")
    private String freeField1;

    @Column(name = "free_field_2")
    private String freeField2;

    @OneToMany(mappedBy = "purchaseOrderDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transferDetails", "categories", "unit", "ecurityUser", "purchaseOrderDetails" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "purchaseOrderDetails", "goodReciveds", "securityUser", "productInventories" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrderDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQtyordered() {
        return this.qtyordered;
    }

    public PurchaseOrderDetails qtyordered(Double qtyordered) {
        this.setQtyordered(qtyordered);
        return this;
    }

    public void setQtyordered(Double qtyordered) {
        this.qtyordered = qtyordered;
    }

    public Integer getGstTaxPercentage() {
        return this.gstTaxPercentage;
    }

    public PurchaseOrderDetails gstTaxPercentage(Integer gstTaxPercentage) {
        this.setGstTaxPercentage(gstTaxPercentage);
        return this;
    }

    public void setGstTaxPercentage(Integer gstTaxPercentage) {
        this.gstTaxPercentage = gstTaxPercentage;
    }

    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    public PurchaseOrderDetails pricePerUnit(Double pricePerUnit) {
        this.setPricePerUnit(pricePerUnit);
        return this;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public PurchaseOrderDetails totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public PurchaseOrderDetails discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public PurchaseOrderDetails lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PurchaseOrderDetails lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFreeField1() {
        return this.freeField1;
    }

    public PurchaseOrderDetails freeField1(String freeField1) {
        this.setFreeField1(freeField1);
        return this;
    }

    public void setFreeField1(String freeField1) {
        this.freeField1 = freeField1;
    }

    public String getFreeField2() {
        return this.freeField2;
    }

    public PurchaseOrderDetails freeField2(String freeField2) {
        this.setFreeField2(freeField2);
        return this;
    }

    public void setFreeField2(String freeField2) {
        this.freeField2 = freeField2;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setPurchaseOrderDetails(null));
        }
        if (products != null) {
            products.forEach(i -> i.setPurchaseOrderDetails(this));
        }
        this.products = products;
    }

    public PurchaseOrderDetails products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public PurchaseOrderDetails addProduct(Product product) {
        this.products.add(product);
        product.setPurchaseOrderDetails(this);
        return this;
    }

    public PurchaseOrderDetails removeProduct(Product product) {
        this.products.remove(product);
        product.setPurchaseOrderDetails(null);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderDetails purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderDetails)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrderDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderDetails{" +
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
            "}";
    }
}
