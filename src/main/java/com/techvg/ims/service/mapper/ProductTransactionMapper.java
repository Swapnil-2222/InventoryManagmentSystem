package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.ProductTransaction;
import com.techvg.ims.service.dto.ProductTransactionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductTransaction} and its DTO {@link ProductTransactionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { WareHouseMapper.class, ProductMapper.class, ProductInventoryMapper.class, SecurityUserMapper.class }
)
public interface ProductTransactionMapper extends EntityMapper<ProductTransactionDTO, ProductTransaction> {
    @Mapping(target = "wareHouse", source = "wareHouse", qualifiedByName = "wareHouseName")
    @Mapping(target = "products", source = "products", qualifiedByName = "productNameSet")
    @Mapping(target = "productInventory", source = "productInventory", qualifiedByName = "id")
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "id")
    ProductTransactionDTO toDto(ProductTransaction s);

    @Mapping(target = "removeProduct", ignore = true)
    ProductTransaction toEntity(ProductTransactionDTO productTransactionDTO);
}
