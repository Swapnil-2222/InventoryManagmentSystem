package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.ProductInventory;
import com.techvg.ims.service.dto.ProductInventoryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductInventory} and its DTO {@link ProductInventoryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ProductMapper.class, PurchaseOrderMapper.class, ProductTransactionMapper.class, WareHouseMapper.class, SecurityUserMapper.class,
    }
)
public interface ProductInventoryMapper extends EntityMapper<ProductInventoryDTO, ProductInventory> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "id")
    @Mapping(target = "productTransaction", source = "productTransaction", qualifiedByName = "id")
    @Mapping(target = "wareHouses", source = "wareHouses", qualifiedByName = "whNameSet")
    @Mapping(target = "securityUsers", source = "securityUsers", qualifiedByName = "loginSet")
    ProductInventoryDTO toDto(ProductInventory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductInventoryDTO toDtoId(ProductInventory productInventory);

    @Mapping(target = "removeWareHouse", ignore = true)
    @Mapping(target = "removeSecurityUser", ignore = true)
    ProductInventory toEntity(ProductInventoryDTO productInventoryDTO);
}
