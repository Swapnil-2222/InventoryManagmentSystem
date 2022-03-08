package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.SecurityUser;
import com.techvg.ims.service.dto.SecurityUserDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SecurityUser} and its DTO {@link SecurityUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecurityPermissionMapper.class, SecurityRoleMapper.class, WareHouseMapper.class })
public interface SecurityUserMapper extends EntityMapper<SecurityUserDTO, SecurityUser> {
    @Mapping(target = "securityPermissions", source = "securityPermissions", qualifiedByName = "nameSet")
    @Mapping(target = "securityRoles", source = "securityRoles", qualifiedByName = "nameSet")
    @Mapping(target = "securityUsers", source = "securityUsers", qualifiedByName = "loginSet")
    SecurityUserDTO toDto(SecurityUser s);

    @Mapping(target = "removeSecurityPermission", ignore = true)
    @Mapping(target = "removeSecurityRole", ignore = true)
    @Mapping(target = "removeSecurityUser", ignore = true)
    SecurityUser toEntity(SecurityUserDTO securityUserDTO);

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    SecurityUserDTO toDtoLogin(SecurityUser securityUser);

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    Set<SecurityUserDTO> toDtoLoginSet(Set<SecurityUser> securityUser);
}
