package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.TransferDetailsApprovals;
import com.techvg.ims.service.dto.TransferDetailsApprovalsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransferDetailsApprovals} and its DTO {@link TransferDetailsApprovalsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecurityUserMapper.class, TransferMapper.class })
public interface TransferDetailsApprovalsMapper extends EntityMapper<TransferDetailsApprovalsDTO, TransferDetailsApprovals> {
    @Mapping(target = "securityUser", source = "securityUser", qualifiedByName = "login")
    @Mapping(target = "transfer", source = "transfer", qualifiedByName = "id")
    TransferDetailsApprovalsDTO toDto(TransferDetailsApprovals s);
}
