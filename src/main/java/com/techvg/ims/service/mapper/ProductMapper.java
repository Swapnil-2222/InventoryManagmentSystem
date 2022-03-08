package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Product;
import com.techvg.ims.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { CategoriesMapper.class, UnitMapper.class, SecurityUserMapper.class, PurchaseOrderDetailsMapper.class }
)
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "id")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "ecurityUser", source = "ecurityUser", qualifiedByName = "login")
    @Mapping(target = "purchaseOrderDetails", source = "purchaseOrderDetails", qualifiedByName = "id")
    ProductDTO toDto(Product s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoId(Product product);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productName", source = "productName")
    ProductDTO toDtoProductName(Product product);
}
