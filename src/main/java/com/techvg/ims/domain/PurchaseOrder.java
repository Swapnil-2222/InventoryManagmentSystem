package com.techvg.ims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techvg.ims.domain.enumeration.OrderType;
import com.techvg.ims.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_po_amount")
    private Double totalPOAmount;

    @Column(name = "total_gst_amount")
    private Double totalGSTAmount;

    @Column(name = "expected_delivery_date")
    private Instant expectedDeliveryDate;

    @Column(name = "po_date")
    private Instant poDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private Status orderStatus;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_mobile")
    private String clientMobile;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "terms_and_condition")
    private String termsAndCondition;

    @Column(name = "notes")
    private String notes;

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

    @OneToMany(mappedBy = "purchaseOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "purchaseOrder" }, allowSetters = true)
    private Set<PurchaseOrderDetails> purchaseOrderDetails = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "purchaseOrder" }, allowSetters = true)
    private Set<GoodsRecived> goodReciveds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "securityPermissions", "securityRoles", "securityUsers", "productInventories" }, allowSetters = true)
    private SecurityUser securityUser;

    @OneToMany(mappedBy = "purchaseOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "consumptionDetails", "product", "purchaseOrder", "productTransaction", "wareHouses", "securityUsers" },
        allowSetters = true
    )
    private Set<ProductInventory> productInventories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPOAmount() {
        return this.totalPOAmount;
    }

    public PurchaseOrder totalPOAmount(Double totalPOAmount) {
        this.setTotalPOAmount(totalPOAmount);
        return this;
    }

    public void setTotalPOAmount(Double totalPOAmount) {
        this.totalPOAmount = totalPOAmount;
    }

    public Double getTotalGSTAmount() {
        return this.totalGSTAmount;
    }

    public PurchaseOrder totalGSTAmount(Double totalGSTAmount) {
        this.setTotalGSTAmount(totalGSTAmount);
        return this;
    }

    public void setTotalGSTAmount(Double totalGSTAmount) {
        this.totalGSTAmount = totalGSTAmount;
    }

    public Instant getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public PurchaseOrder expectedDeliveryDate(Instant expectedDeliveryDate) {
        this.setExpectedDeliveryDate(expectedDeliveryDate);
        return this;
    }

    public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Instant getPoDate() {
        return this.poDate;
    }

    public PurchaseOrder poDate(Instant poDate) {
        this.setPoDate(poDate);
        return this;
    }

    public void setPoDate(Instant poDate) {
        this.poDate = poDate;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public PurchaseOrder orderType(OrderType orderType) {
        this.setOrderType(orderType);
        return this;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Status getOrderStatus() {
        return this.orderStatus;
    }

    public PurchaseOrder orderStatus(Status orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getClientName() {
        return this.clientName;
    }

    public PurchaseOrder clientName(String clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientMobile() {
        return this.clientMobile;
    }

    public PurchaseOrder clientMobile(String clientMobile) {
        this.setClientMobile(clientMobile);
        return this;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public PurchaseOrder clientEmail(String clientEmail) {
        this.setClientEmail(clientEmail);
        return this;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getTermsAndCondition() {
        return this.termsAndCondition;
    }

    public PurchaseOrder termsAndCondition(String termsAndCondition) {
        this.setTermsAndCondition(termsAndCondition);
        return this;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public String getNotes() {
        return this.notes;
    }

    public PurchaseOrder notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public PurchaseOrder lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PurchaseOrder lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getFreeField1() {
        return this.freeField1;
    }

    public PurchaseOrder freeField1(String freeField1) {
        this.setFreeField1(freeField1);
        return this;
    }

    public void setFreeField1(String freeField1) {
        this.freeField1 = freeField1;
    }

    public String getFreeField2() {
        return this.freeField2;
    }

    public PurchaseOrder freeField2(String freeField2) {
        this.setFreeField2(freeField2);
        return this;
    }

    public void setFreeField2(String freeField2) {
        this.freeField2 = freeField2;
    }

    public Set<PurchaseOrderDetails> getPurchaseOrderDetails() {
        return this.purchaseOrderDetails;
    }

    public void setPurchaseOrderDetails(Set<PurchaseOrderDetails> purchaseOrderDetails) {
        if (this.purchaseOrderDetails != null) {
            this.purchaseOrderDetails.forEach(i -> i.setPurchaseOrder(null));
        }
        if (purchaseOrderDetails != null) {
            purchaseOrderDetails.forEach(i -> i.setPurchaseOrder(this));
        }
        this.purchaseOrderDetails = purchaseOrderDetails;
    }

    public PurchaseOrder purchaseOrderDetails(Set<PurchaseOrderDetails> purchaseOrderDetails) {
        this.setPurchaseOrderDetails(purchaseOrderDetails);
        return this;
    }

    public PurchaseOrder addPurchaseOrderDetails(PurchaseOrderDetails purchaseOrderDetails) {
        this.purchaseOrderDetails.add(purchaseOrderDetails);
        purchaseOrderDetails.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removePurchaseOrderDetails(PurchaseOrderDetails purchaseOrderDetails) {
        this.purchaseOrderDetails.remove(purchaseOrderDetails);
        purchaseOrderDetails.setPurchaseOrder(null);
        return this;
    }

    public Set<GoodsRecived> getGoodReciveds() {
        return this.goodReciveds;
    }

    public void setGoodReciveds(Set<GoodsRecived> goodsReciveds) {
        if (this.goodReciveds != null) {
            this.goodReciveds.forEach(i -> i.setPurchaseOrder(null));
        }
        if (goodsReciveds != null) {
            goodsReciveds.forEach(i -> i.setPurchaseOrder(this));
        }
        this.goodReciveds = goodsReciveds;
    }

    public PurchaseOrder goodReciveds(Set<GoodsRecived> goodsReciveds) {
        this.setGoodReciveds(goodsReciveds);
        return this;
    }

    public PurchaseOrder addGoodRecived(GoodsRecived goodsRecived) {
        this.goodReciveds.add(goodsRecived);
        goodsRecived.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removeGoodRecived(GoodsRecived goodsRecived) {
        this.goodReciveds.remove(goodsRecived);
        goodsRecived.setPurchaseOrder(null);
        return this;
    }

    public SecurityUser getSecurityUser() {
        return this.securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    public PurchaseOrder securityUser(SecurityUser securityUser) {
        this.setSecurityUser(securityUser);
        return this;
    }

    public Set<ProductInventory> getProductInventories() {
        return this.productInventories;
    }

    public void setProductInventories(Set<ProductInventory> productInventories) {
        if (this.productInventories != null) {
            this.productInventories.forEach(i -> i.setPurchaseOrder(null));
        }
        if (productInventories != null) {
            productInventories.forEach(i -> i.setPurchaseOrder(this));
        }
        this.productInventories = productInventories;
    }

    public PurchaseOrder productInventories(Set<ProductInventory> productInventories) {
        this.setProductInventories(productInventories);
        return this;
    }

    public PurchaseOrder addProductInventory(ProductInventory productInventory) {
        this.productInventories.add(productInventory);
        productInventory.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removeProductInventory(ProductInventory productInventory) {
        this.productInventories.remove(productInventory);
        productInventory.setPurchaseOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrder{" +
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
            "}";
    }
}
