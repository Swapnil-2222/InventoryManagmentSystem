package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.WareHouse;
import com.techvg.ims.service.dto.WareHouseDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WareHouse} and its DTO {@link WareHouseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WareHouseMapper extends EntityMapper<WareHouseDTO, WareHouse> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<WareHouseDTO> toDtoIdSet(Set<WareHouse> wareHouse);

    @Named("whNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "whName", source = "whName")
    Set<WareHouseDTO> toDtoWhNameSet(Set<WareHouse> wareHouse);

    @Named("whName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "whName", source = "whName")
    WareHouseDTO toDtoWhName(WareHouse wareHouse);
}
