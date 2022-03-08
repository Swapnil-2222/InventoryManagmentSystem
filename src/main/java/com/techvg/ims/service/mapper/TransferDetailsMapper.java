package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.TransferDetails;
import com.techvg.ims.service.dto.TransferDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransferDetails} and its DTO {@link TransferDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { WareHouseMapper.class, ProductMapper.class, TransferMapper.class })
public interface TransferDetailsMapper extends EntityMapper<TransferDetailsDTO, TransferDetails> {
    @Mapping(target = "wareHouse", source = "wareHouse", qualifiedByName = "whName")
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "transfer", source = "transfer", qualifiedByName = "id")
    TransferDetailsDTO toDto(TransferDetails s);
}
