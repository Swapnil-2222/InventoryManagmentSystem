package com.techvg.ims.service.criteria;

import com.techvg.ims.domain.enumeration.NotificationType;
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
 * Criteria class for the {@link com.techvg.ims.domain.Notification} entity. This class is used
 * in {@link com.techvg.ims.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter massage;

    private NotificationTypeFilter notificationType;

    private BooleanFilter isActionRequired;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter ecurityUserId;

    private LongFilter wareHouseId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.massage = other.massage == null ? null : other.massage.copy();
        this.notificationType = other.notificationType == null ? null : other.notificationType.copy();
        this.isActionRequired = other.isActionRequired == null ? null : other.isActionRequired.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.ecurityUserId = other.ecurityUserId == null ? null : other.ecurityUserId.copy();
        this.wareHouseId = other.wareHouseId == null ? null : other.wareHouseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getMassage() {
        return massage;
    }

    public StringFilter massage() {
        if (massage == null) {
            massage = new StringFilter();
        }
        return massage;
    }

    public void setMassage(StringFilter massage) {
        this.massage = massage;
    }

    public NotificationTypeFilter getNotificationType() {
        return notificationType;
    }

    public NotificationTypeFilter notificationType() {
        if (notificationType == null) {
            notificationType = new NotificationTypeFilter();
        }
        return notificationType;
    }

    public void setNotificationType(NotificationTypeFilter notificationType) {
        this.notificationType = notificationType;
    }

    public BooleanFilter getIsActionRequired() {
        return isActionRequired;
    }

    public BooleanFilter isActionRequired() {
        if (isActionRequired == null) {
            isActionRequired = new BooleanFilter();
        }
        return isActionRequired;
    }

    public void setIsActionRequired(BooleanFilter isActionRequired) {
        this.isActionRequired = isActionRequired;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(massage, that.massage) &&
            Objects.equals(notificationType, that.notificationType) &&
            Objects.equals(isActionRequired, that.isActionRequired) &&
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
            massage,
            notificationType,
            isActionRequired,
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
        return "NotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (massage != null ? "massage=" + massage + ", " : "") +
            (notificationType != null ? "notificationType=" + notificationType + ", " : "") +
            (isActionRequired != null ? "isActionRequired=" + isActionRequired + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (ecurityUserId != null ? "ecurityUserId=" + ecurityUserId + ", " : "") +
            (wareHouseId != null ? "wareHouseId=" + wareHouseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
