package hu.tsystems.devlad.service;

import hu.tsystems.devlad.domain.SkillRequest;
import hu.tsystems.devlad.repository.SkillRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing SkillRequest.
 */
@Service
@Transactional
public class SkillRequestService {

    private final Logger log = LoggerFactory.getLogger(SkillRequestService.class);
    
    private final SkillRequestRepository skillRequestRepository;

    public SkillRequestService(SkillRequestRepository skillRequestRepository) {
        this.skillRequestRepository = skillRequestRepository;
    }

    /**
     * Save a skillRequest.
     *
     * @param skillRequest the entity to save
     * @return the persisted entity
     */
    public SkillRequest save(SkillRequest skillRequest) {
        log.debug("Request to save SkillRequest : {}", skillRequest);
        SkillRequest result = skillRequestRepository.save(skillRequest);
        return result;
    }

    /**
     *  Get all the skillRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SkillRequest> findAll(Pageable pageable) {
        log.debug("Request to get all SkillRequests");
        Page<SkillRequest> result = skillRequestRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one skillRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SkillRequest findOne(Long id) {
        log.debug("Request to get SkillRequest : {}", id);
        SkillRequest skillRequest = skillRequestRepository.findOne(id);
        return skillRequest;
    }

    /**
     *  Delete the  skillRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SkillRequest : {}", id);
        skillRequestRepository.delete(id);
    }
}
