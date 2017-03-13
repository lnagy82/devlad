package hu.tsystems.devlad.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.tsystems.devlad.domain.LearnedSkill;
import hu.tsystems.devlad.service.LearnedSkillService;
import hu.tsystems.devlad.web.rest.util.HeaderUtil;
import hu.tsystems.devlad.web.rest.util.PaginationUtil;
import hu.tsystems.devlad.web.rest.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LearnedSkill.
 */
@RestController
@RequestMapping("/api")
public class LearnedSkillResource {

    private final Logger log = LoggerFactory.getLogger(LearnedSkillResource.class);

    private static final String ENTITY_NAME = "learnedSkill";
        
    private final LearnedSkillService learnedSkillService;

    public LearnedSkillResource(LearnedSkillService learnedSkillService) {
        this.learnedSkillService = learnedSkillService;
    }

    /**
     * POST  /learned-skills : Create a new learnedSkill.
     *
     * @param learnedSkill the learnedSkill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new learnedSkill, or with status 400 (Bad Request) if the learnedSkill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/learned-skills")
    @Timed
    public ResponseEntity<LearnedSkill> createLearnedSkill(@Valid @RequestBody LearnedSkill learnedSkill) throws URISyntaxException {
        log.debug("REST request to save LearnedSkill : {}", learnedSkill);
        if (learnedSkill.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new learnedSkill cannot already have an ID")).body(null);
        }
        LearnedSkill result = learnedSkillService.save(learnedSkill);
        return ResponseEntity.created(new URI("/api/learned-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /learned-skills : Updates an existing learnedSkill.
     *
     * @param learnedSkill the learnedSkill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated learnedSkill,
     * or with status 400 (Bad Request) if the learnedSkill is not valid,
     * or with status 500 (Internal Server Error) if the learnedSkill couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/learned-skills")
    @Timed
    public ResponseEntity<LearnedSkill> updateLearnedSkill(@Valid @RequestBody LearnedSkill learnedSkill) throws URISyntaxException {
        log.debug("REST request to update LearnedSkill : {}", learnedSkill);
        if (learnedSkill.getId() == null) {
            return createLearnedSkill(learnedSkill);
        }
        LearnedSkill result = learnedSkillService.save(learnedSkill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, learnedSkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /learned-skills : get all the learnedSkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of learnedSkills in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/learned-skills")
    @Timed
    public ResponseEntity<List<LearnedSkill>> getAllLearnedSkills(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LearnedSkills");
        Page<LearnedSkill> page = learnedSkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/learned-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /learned-skills : get all the learnedSkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of learnedSkills in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/learned-skills/developer/{developerId}")
    @Timed
    public ResponseEntity<List<LearnedSkill>> getLearnedSkillsByDeveloper(@ApiParam Pageable pageable, @PathVariable Long developerId)
        throws URISyntaxException {
        log.debug("REST request to get a page of LearnedSkills");
        Page<LearnedSkill> page = learnedSkillService.findAllByDeveloper(pageable, developerId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/learned-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /learned-skills/:id : get the "id" learnedSkill.
     *
     * @param id the id of the learnedSkill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the learnedSkill, or with status 404 (Not Found)
     */
    @GetMapping("/learned-skills/{id}")
    @Timed
    public ResponseEntity<LearnedSkill> getLearnedSkill(@PathVariable Long id) {
        log.debug("REST request to get LearnedSkill : {}", id);
        LearnedSkill learnedSkill = learnedSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(learnedSkill));
    }

    /**
     * DELETE  /learned-skills/:id : delete the "id" learnedSkill.
     *
     * @param id the id of the learnedSkill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/learned-skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteLearnedSkill(@PathVariable Long id) {
        log.debug("REST request to delete LearnedSkill : {}", id);
        learnedSkillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
