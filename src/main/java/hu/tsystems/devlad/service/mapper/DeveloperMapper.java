package hu.tsystems.devlad.service.mapper;

import hu.tsystems.devlad.domain.*;
import hu.tsystems.devlad.service.dto.DeveloperDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Developer and its DTO DeveloperDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeveloperMapper {

    DeveloperDTO developerToDeveloperDTO(Developer developer);

    List<DeveloperDTO> developersToDeveloperDTOs(List<Developer> developers);

    Developer developerDTOToDeveloper(DeveloperDTO developerDTO);

    List<Developer> developerDTOsToDevelopers(List<DeveloperDTO> developerDTOs);
}
