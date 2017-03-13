package hu.tsystems.devlad.service;

import hu.tsystems.devlad.domain.LearnedSkill;
import hu.tsystems.devlad.repository.LearnedSkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing LearnedSkill.
 */
@Service
@Transactional
public class LearnedSkillService {

    private final Logger log = LoggerFactory.getLogger(LearnedSkillService.class);
    
    private final LearnedSkillRepository learnedSkillRepository;

    public LearnedSkillService(LearnedSkillRepository learnedSkillRepository) {
        this.learnedSkillRepository = learnedSkillRepository;
    }

    /**
     * Save a learnedSkill.
     *
     * @param learnedSkill the entity to save
     * @return the persisted entity
     */
    public LearnedSkill save(LearnedSkill learnedSkill) {
        log.debug("Request to save LearnedSkill : {}", learnedSkill);
        LearnedSkill result = learnedSkillRepository.save(learnedSkill);
        return result;
    }

    /**
     *  Get all the learnedSkills.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LearnedSkill> findAll(Pageable pageable) {
        log.debug("Request to get all LearnedSkills");
        Page<LearnedSkill> result = learnedSkillRepository.findAll(pageable);
        return result;
    }
    
    /**
     *  Get all the learnedSkills.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LearnedSkill> findAllByDeveloper(Pageable pageable, Long developerId) {
        log.debug("Request to get all LearnedSkills");
        Page<LearnedSkill> result = learnedSkillRepository.findAllByDeveloperId(pageable, developerId);
        return result;
    }

    /**
     *  Get one learnedSkill by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LearnedSkill findOne(Long id) {
        log.debug("Request to get LearnedSkill : {}", id);
        LearnedSkill learnedSkill = learnedSkillRepository.findOne(id);
        return learnedSkill;
    }

    /**
     *  Delete the  learnedSkill by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LearnedSkill : {}", id);
        learnedSkillRepository.delete(id);
    }
}
