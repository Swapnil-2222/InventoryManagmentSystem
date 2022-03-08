package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.PurchaseOrder;
import com.techvg.ims.service.dto.PurchaseOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrder} and its DTO {@link PurchaseOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecurityUserMapper.class })
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDTO, PurchaseOrder> {
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "login")
    PurchaseOrderDTO toDto(PurchaseOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseOrderDTO toDtoId(PurchaseOrder purchaseOrder);
}
