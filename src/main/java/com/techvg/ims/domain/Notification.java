package com.techvg.ims.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techvg.ims.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "massage", nullable = false)
    private String massage;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "is_action_required")
    private Boolean isActionRequired;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
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

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMassage() {
        return this.massage;
    }

    public Notification massage(String massage) {
        this.setMassage(massage);
        return this;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public Notification notificationType(NotificationType notificationType) {
        this.setNotificationType(notificationType);
        return this;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Boolean getIsActionRequired() {
        return this.isActionRequired;
    }

    public Notification isActionRequired(Boolean isActionRequired) {
        this.setIsActionRequired(isActionRequired);
        return this;
    }

    public void setIsActionRequired(Boolean isActionRequired) {
        this.isActionRequired = isActionRequired;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Notification lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Notification lastModifiedBy(String lastModifiedBy) {
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

    public Notification ecurityUser(SecurityUser securityUser) {
        this.setEcurityUser(securityUser);
        return this;
    }

    public WareHouse getWareHouse() {
        return this.wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    public Notification wareHouse(WareHouse wareHouse) {
        this.setWareHouse(wareHouse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", massage='" + getMassage() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            ", isActionRequired='" + getIsActionRequired() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
