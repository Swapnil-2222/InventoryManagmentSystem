package com.techvg.ims.service.dto;

import com.techvg.ims.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.techvg.ims.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String massage;

    private NotificationType notificationType;

    private Boolean isActionRequired;

    @NotNull
    private Instant lastModified;

    @NotNull
    private String lastModifiedBy;

    private SecurityUserDTO ecurityUser;

    private WareHouseDTO wareHouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Boolean getIsActionRequired() {
        return isActionRequired;
    }

    public void setIsActionRequired(Boolean isActionRequired) {
        this.isActionRequired = isActionRequired;
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

    public SecurityUserDTO getEcurityUser() {
        return ecurityUser;
    }

    public void setEcurityUser(SecurityUserDTO ecurityUser) {
        this.ecurityUser = ecurityUser;
    }

    public WareHouseDTO getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(WareHouseDTO wareHouse) {
        this.wareHouse = wareHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", massage='" + getMassage() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            ", isActionRequired='" + getIsActionRequired() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", ecurityUser=" + getEcurityUser() +
            ", wareHouse=" + getWareHouse() +
            "}";
    }
}
