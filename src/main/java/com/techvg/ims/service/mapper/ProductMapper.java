package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Product;
import com.techvg.ims.service.dto.ProductDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PurchaseOrderDetailsMapper.class, CategoriesMapper.class, UnitMapper.class, SecurityUserMapper.class }
)
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "purchaseOrderDetails", source = "purchaseOrderDetails", qualifiedByName = "id")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "id")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "id")
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

    @Named("productNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productName", source = "productName")
    Set<ProductDTO> toDtoProductNameSet(Set<Product> product);
}
