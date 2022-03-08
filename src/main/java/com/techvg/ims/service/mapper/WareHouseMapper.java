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
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WareHouseDTO toDtoId(WareHouse wareHouse);

    @Named("wareHouseNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "wareHouseName", source = "wareHouseName")
    Set<WareHouseDTO> toDtoWareHouseNameSet(Set<WareHouse> wareHouse);

    @Named("wareHouseName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "wareHouseName", source = "wareHouseName")
    WareHouseDTO toDtoWareHouseName(WareHouse wareHouse);
}
