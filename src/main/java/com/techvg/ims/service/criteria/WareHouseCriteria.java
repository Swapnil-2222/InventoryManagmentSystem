package com.techvg.ims.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.techvg.ims.domain.WareHouse} entity. This class is used
 * in {@link com.techvg.ims.web.rest.WareHouseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ware-houses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class WareHouseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter wareHouseName;

    private StringFilter address;

    private IntegerFilter pincode;

    private StringFilter city;

    private StringFilter state;

    private StringFilter country;

    private StringFilter gstDetails;

    private StringFilter managerName;

    private StringFilter managerEmail;

    private StringFilter managerContact;

    private StringFilter contact;

    private BooleanFilter isDeleted;

    private BooleanFilter isActive;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter productInventoryId;

    private Boolean distinct;

    public WareHouseCriteria() {}

    public WareHouseCriteria(WareHouseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.wareHouseName = other.wareHouseName == null ? null : other.wareHouseName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.pincode = other.pincode == null ? null : other.pincode.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.gstDetails = other.gstDetails == null ? null : other.gstDetails.copy();
        this.managerName = other.managerName == null ? null : other.managerName.copy();
        this.managerEmail = other.managerEmail == null ? null : other.managerEmail.copy();
        this.managerContact = other.managerContact == null ? null : other.managerContact.copy();
        this.contact = other.contact == null ? null : other.contact.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.productInventoryId = other.productInventoryId == null ? null : other.productInventoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WareHouseCriteria copy() {
        return new WareHouseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getWareHouseName() {
        return wareHouseName;
    }

    public StringFilter wareHouseName() {
        if (wareHouseName == null) {
            wareHouseName = new StringFilter();
        }
        return wareHouseName;
    }

    public void setWareHouseName(StringFilter wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public IntegerFilter getPincode() {
        return pincode;
    }

    public IntegerFilter pincode() {
        if (pincode == null) {
            pincode = new IntegerFilter();
        }
        return pincode;
    }

    public void setPincode(IntegerFilter pincode) {
        this.pincode = pincode;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getGstDetails() {
        return gstDetails;
    }

    public StringFilter gstDetails() {
        if (gstDetails == null) {
            gstDetails = new StringFilter();
        }
        return gstDetails;
    }

    public void setGstDetails(StringFilter gstDetails) {
        this.gstDetails = gstDetails;
    }

    public StringFilter getManagerName() {
        return managerName;
    }

    public StringFilter managerName() {
        if (managerName == null) {
            managerName = new StringFilter();
        }
        return managerName;
    }

    public void setManagerName(StringFilter managerName) {
        this.managerName = managerName;
    }

    public StringFilter getManagerEmail() {
        return managerEmail;
    }

    public StringFilter managerEmail() {
        if (managerEmail == null) {
            managerEmail = new StringFilter();
        }
        return managerEmail;
    }

    public void setManagerEmail(StringFilter managerEmail) {
        this.managerEmail = managerEmail;
    }

    public StringFilter getManagerContact() {
        return managerContact;
    }

    public StringFilter managerContact() {
        if (managerContact == null) {
            managerContact = new StringFilter();
        }
        return managerContact;
    }

    public void setManagerContact(StringFilter managerContact) {
        this.managerContact = managerContact;
    }

    public StringFilter getContact() {
        return contact;
    }

    public StringFilter contact() {
        if (contact == null) {
            contact = new StringFilter();
        }
        return contact;
    }

    public void setContact(StringFilter contact) {
        this.contact = contact;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public InstantFilter getLastModified() {
        return lastModified;
    }

    public InstantFilter lastModified() {
        if (lastModified == null) {
            lastModified = new InstantFilter();
        }
        return lastModified;
    }

    public void setLastModified(InstantFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LongFilter getProductInventoryId() {
        return productInventoryId;
    }

    public LongFilter productInventoryId() {
        if (productInventoryId == null) {
            productInventoryId = new LongFilter();
        }
        return productInventoryId;
    }

    public void setProductInventoryId(LongFilter productInventoryId) {
        this.productInventoryId = productInventoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WareHouseCriteria that = (WareHouseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(wareHouseName, that.wareHouseName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(pincode, that.pincode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(country, that.country) &&
            Objects.equals(gstDetails, that.gstDetails) &&
            Objects.equals(managerName, that.managerName) &&
            Objects.equals(managerEmail, that.managerEmail) &&
            Objects.equals(managerContact, that.managerContact) &&
            Objects.equals(contact, that.contact) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(productInventoryId, that.productInventoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            wareHouseName,
            address,
            pincode,
            city,
            state,
            country,
            gstDetails,
            managerName,
            managerEmail,
            managerContact,
            contact,
            isDeleted,
            isActive,
            lastModified,
            lastModifiedBy,
            productInventoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WareHouseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (wareHouseName != null ? "wareHouseName=" + wareHouseName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (pincode != null ? "pincode=" + pincode + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (gstDetails != null ? "gstDetails=" + gstDetails + ", " : "") +
            (managerName != null ? "managerName=" + managerName + ", " : "") +
            (managerEmail != null ? "managerEmail=" + managerEmail + ", " : "") +
            (managerContact != null ? "managerContact=" + managerContact + ", " : "") +
            (contact != null ? "contact=" + contact + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (productInventoryId != null ? "productInventoryId=" + productInventoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
