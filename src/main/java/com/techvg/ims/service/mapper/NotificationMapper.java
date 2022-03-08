package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Notification;
import com.techvg.ims.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecurityUserMapper.class, WareHouseMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "ecurityUser", source = "ecurityUser", qualifiedByName = "login")
    @Mapping(target = "wareHouse", source = "wareHouse", qualifiedByName = "whName")
    NotificationDTO toDto(Notification s);
}
