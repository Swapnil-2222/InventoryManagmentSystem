package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Transfer;
import com.techvg.ims.service.dto.TransferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transfer} and its DTO {@link TransferDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecurityUserMapper.class, WareHouseMapper.class })
public interface TransferMapper extends EntityMapper<TransferDTO, Transfer> {
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "login")
    @Mapping(target = "wareHouse", source = "wareHouse", qualifiedByName = "wareHouseName")
    TransferDTO toDto(Transfer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransferDTO toDtoId(Transfer transfer);
}
