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
 * A WareHouse.
 */
@Entity
@Table(name = "ware_house")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WareHouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ware_house_name")
    private String wareHouseName;

    @Column(name = "address")
    private String address;

    @Column(name = "pincode")
    private Integer pincode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "gst_details")
    private String gstDetails;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "manager_email")
    private String managerEmail;

    @Column(name = "manager_contact")
    private String managerContact;

    @Column(name = "contact")
    private String contact;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @ManyToMany(mappedBy = "wareHouses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "consumptionDetails", "productTransactions", "product", "purchaseOrder", "wareHouses", "securityUsers" },
        allowSetters = true
    )
    private Set<ProductInventory> productInventories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WareHouse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWareHouseName() {
        return this.wareHouseName;
    }

    public WareHouse wareHouseName(String wareHouseName) {
        this.setWareHouseName(wareHouseName);
        return this;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public String getAddress() {
        return this.address;
    }

    public WareHouse address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPincode() {
        return this.pincode;
    }

    public WareHouse pincode(Integer pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return this.city;
    }

    public WareHouse city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public WareHouse state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public WareHouse country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGstDetails() {
        return this.gstDetails;
    }

    public WareHouse gstDetails(String gstDetails) {
        this.setGstDetails(gstDetails);
        return this;
    }

    public void setGstDetails(String gstDetails) {
        this.gstDetails = gstDetails;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public WareHouse managerName(String managerName) {
        this.setManagerName(managerName);
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return this.managerEmail;
    }

    public WareHouse managerEmail(String managerEmail) {
        this.setManagerEmail(managerEmail);
        return this;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerContact() {
        return this.managerContact;
    }

    public WareHouse managerContact(String managerContact) {
        this.setManagerContact(managerContact);
        return this;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getContact() {
        return this.contact;
    }

    public WareHouse contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public WareHouse isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public WareHouse isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public WareHouse lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public WareHouse lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<ProductInventory> getProductInventories() {
        return this.productInventories;
    }

    public void setProductInventories(Set<ProductInventory> productInventories) {
        if (this.productInventories != null) {
            this.productInventories.forEach(i -> i.removeWareHouse(this));
        }
        if (productInventories != null) {
            productInventories.forEach(i -> i.addWareHouse(this));
        }
        this.productInventories = productInventories;
    }

    public WareHouse productInventories(Set<ProductInventory> productInventories) {
        this.setProductInventories(productInventories);
        return this;
    }

    public WareHouse addProductInventory(ProductInventory productInventory) {
        this.productInventories.add(productInventory);
        productInventory.getWareHouses().add(this);
        return this;
    }

    public WareHouse removeProductInventory(ProductInventory productInventory) {
        this.productInventories.remove(productInventory);
        productInventory.getWareHouses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WareHouse)) {
            return false;
        }
        return id != null && id.equals(((WareHouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WareHouse{" +
            "id=" + getId() +
            ", wareHouseName='" + getWareHouseName() + "'" +
            ", address='" + getAddress() + "'" +
            ", pincode=" + getPincode() +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", gstDetails='" + getGstDetails() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", managerEmail='" + getManagerEmail() + "'" +
            ", managerContact='" + getManagerContact() + "'" +
            ", contact='" + getContact() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
