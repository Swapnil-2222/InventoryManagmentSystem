package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Categories;
import com.techvg.ims.service.dto.CategoriesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categories} and its DTO {@link CategoriesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoriesMapper extends EntityMapper<CategoriesDTO, Categories> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriesDTO toDtoId(Categories categories);
}
