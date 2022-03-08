package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.PurchaseOrderDetails;
import com.techvg.ims.service.dto.PurchaseOrderDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrderDetails} and its DTO {@link PurchaseOrderDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { PurchaseOrderMapper.class })
public interface PurchaseOrderDetailsMapper extends EntityMapper<PurchaseOrderDetailsDTO, PurchaseOrderDetails> {
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "id")
    PurchaseOrderDetailsDTO toDto(PurchaseOrderDetails s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseOrderDetailsDTO toDtoId(PurchaseOrderDetails purchaseOrderDetails);
}
