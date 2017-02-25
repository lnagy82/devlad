package hu.tsystems.devlad.service.mapper;

import hu.tsystems.devlad.domain.*;
import hu.tsystems.devlad.service.dto.DeveloperDTO;

import org.mapstruct.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity Developer and its DTO DeveloperDTO.
 */
@Service("developerMapper")
@Transactional
public class DeveloperMapperBean implements  DeveloperMapper{

	@Override
	public DeveloperDTO developerToDeveloperDTO(Developer developer) {

		DeveloperDTO ret = new DeveloperDTO();
		ret.setDescription(developer.getDescription());
		ret.setExperiencePoints(developer.getExperiencePoints());
		ret.setId(developer.getId());
		ret.setIdentifier(developer.getIdentifier());
		ret.setLevel(developer.getLevel());
		return ret;
	}

	@Override
	public List<DeveloperDTO> developersToDeveloperDTOs(List<Developer> developers) {

		List<DeveloperDTO> retList = new ArrayList<DeveloperDTO>();
		
		for (Developer developer : developers) {
			retList.add(developerToDeveloperDTO(developer));
		}
		
		return retList;
	}

	@Override
	public Developer developerDTOToDeveloper(DeveloperDTO developerDTO) {
		
		Developer ret = new Developer();
		ret.setDescription(developerDTO.getDescription());
		ret.setExperiencePoints(developerDTO.getExperiencePoints());
		ret.setId(developerDTO.getId());
		ret.setIdentifier(developerDTO.getIdentifier());
		ret.setLevel(developerDTO.getLevel());
		return ret;
	}

	@Override
	public List<Developer> developerDTOsToDevelopers(List<DeveloperDTO> developerDTOs) {
		List<Developer> retList = new ArrayList<Developer>();
		
		for (DeveloperDTO developer : developerDTOs) {
			retList.add(developerDTOToDeveloper(developer));
		}
		
		return retList;
	}
}
