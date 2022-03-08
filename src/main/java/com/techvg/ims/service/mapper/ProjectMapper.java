package com.techvg.ims.service.mapper;

import com.techvg.ims.domain.Project;
import com.techvg.ims.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProjectDTO toDtoName(Project project);
}
