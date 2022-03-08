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
 * Criteria class for the {@link com.techvg.ims.domain.ProductTransaction} entity. This class is used
 * in {@link com.techvg.ims.web.rest.ProductTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter qtySold;

    private DoubleFilter pricePerUnit;

    private StringFilter lotNumber;

    private InstantFilter expirydate;

    private DoubleFilter totalAmount;

    private DoubleFilter gstAmount;

    private StringFilter description;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter ecurityUserId;

    private LongFilter wareHouseId;

    private Boolean distinct;

    public ProductTransactionCriteria() {}

    public ProductTransactionCriteria(ProductTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qtySold = other.qtySold == null ? null : other.qtySold.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.lotNumber = other.lotNumber == null ? null : other.lotNumber.copy();
        this.expirydate = other.expirydate == null ? null : other.expirydate.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.gstAmount = other.gstAmount == null ? null : other.gstAmount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.ecurityUserId = other.ecurityUserId == null ? null : other.ecurityUserId.copy();
        this.wareHouseId = other.wareHouseId == null ? null : other.wareHouseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductTransactionCriteria copy() {
        return new ProductTransactionCriteria(this);
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

    public DoubleFilter getQtySold() {
        return qtySold;
    }

    public DoubleFilter qtySold() {
        if (qtySold == null) {
            qtySold = new DoubleFilter();
        }
        return qtySold;
    }

    public void setQtySold(DoubleFilter qtySold) {
        this.qtySold = qtySold;
    }

    public DoubleFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public DoubleFilter pricePerUnit() {
        if (pricePerUnit == null) {
            pricePerUnit = new DoubleFilter();
        }
        return pricePerUnit;
    }

    public void setPricePerUnit(DoubleFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public StringFilter getLotNumber() {
        return lotNumber;
    }

    public StringFilter lotNumber() {
        if (lotNumber == null) {
            lotNumber = new StringFilter();
        }
        return lotNumber;
    }

    public void setLotNumber(StringFilter lotNumber) {
        this.lotNumber = lotNumber;
    }

    public InstantFilter getExpirydate() {
        return expirydate;
    }

    public InstantFilter expirydate() {
        if (expirydate == null) {
            expirydate = new InstantFilter();
        }
        return expirydate;
    }

    public void setExpirydate(InstantFilter expirydate) {
        this.expirydate = expirydate;
    }

    public DoubleFilter getTotalAmount() {
        return totalAmount;
    }

    public DoubleFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new DoubleFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(DoubleFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DoubleFilter getGstAmount() {
        return gstAmount;
    }

    public DoubleFilter gstAmount() {
        if (gstAmount == null) {
            gstAmount = new DoubleFilter();
        }
        return gstAmount;
    }

    public void setGstAmount(DoubleFilter gstAmount) {
        this.gstAmount = gstAmount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getEcurityUserId() {
        return ecurityUserId;
    }

    public LongFilter ecurityUserId() {
        if (ecurityUserId == null) {
            ecurityUserId = new LongFilter();
        }
        return ecurityUserId;
    }

    public void setEcurityUserId(LongFilter ecurityUserId) {
        this.ecurityUserId = ecurityUserId;
    }

    public LongFilter getWareHouseId() {
        return wareHouseId;
    }

    public LongFilter wareHouseId() {
        if (wareHouseId == null) {
            wareHouseId = new LongFilter();
        }
        return wareHouseId;
    }

    public void setWareHouseId(LongFilter wareHouseId) {
        this.wareHouseId = wareHouseId;
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
        final ProductTransactionCriteria that = (ProductTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(qtySold, that.qtySold) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(lotNumber, that.lotNumber) &&
            Objects.equals(expirydate, that.expirydate) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(gstAmount, that.gstAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(ecurityUserId, that.ecurityUserId) &&
            Objects.equals(wareHouseId, that.wareHouseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            qtySold,
            pricePerUnit,
            lotNumber,
            expirydate,
            totalAmount,
            gstAmount,
            description,
            lastModified,
            lastModifiedBy,
            ecurityUserId,
            wareHouseId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qtySold != null ? "qtySold=" + qtySold + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (lotNumber != null ? "lotNumber=" + lotNumber + ", " : "") +
            (expirydate != null ? "expirydate=" + expirydate + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (gstAmount != null ? "gstAmount=" + gstAmount + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (ecurityUserId != null ? "ecurityUserId=" + ecurityUserId + ", " : "") +
            (wareHouseId != null ? "wareHouseId=" + wareHouseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
