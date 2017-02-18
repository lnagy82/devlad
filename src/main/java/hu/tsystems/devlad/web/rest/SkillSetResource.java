package hu.tsystems.devlad.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.tsystems.devlad.domain.SkillSet;

import hu.tsystems.devlad.repository.SkillSetRepository;
import hu.tsystems.devlad.web.rest.util.HeaderUtil;
import hu.tsystems.devlad.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing SkillSet.
 */
@RestController
@RequestMapping("/api")
public class SkillSetResource {

    private final Logger log = LoggerFactory.getLogger(SkillSetResource.class);

    private static final String ENTITY_NAME = "skillSet";
        
    private final SkillSetRepository skillSetRepository;

    public SkillSetResource(SkillSetRepository skillSetRepository) {
        this.skillSetRepository = skillSetRepository;
    }

    /**
     * POST  /skill-sets : Create a new skillSet.
     *
     * @param skillSet the skillSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillSet, or with status 400 (Bad Request) if the skillSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skill-sets")
    @Timed
    public ResponseEntity<SkillSet> createSkillSet(@Valid @RequestBody SkillSet skillSet) throws URISyntaxException {
        log.debug("REST request to save SkillSet : {}", skillSet);
        if (skillSet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new skillSet cannot already have an ID")).body(null);
        }
        SkillSet result = skillSetRepository.save(skillSet);
        return ResponseEntity.created(new URI("/api/skill-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skill-sets : Updates an existing skillSet.
     *
     * @param skillSet the skillSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillSet,
     * or with status 400 (Bad Request) if the skillSet is not valid,
     * or with status 500 (Internal Server Error) if the skillSet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skill-sets")
    @Timed
    public ResponseEntity<SkillSet> updateSkillSet(@Valid @RequestBody SkillSet skillSet) throws URISyntaxException {
        log.debug("REST request to update SkillSet : {}", skillSet);
        if (skillSet.getId() == null) {
            return createSkillSet(skillSet);
        }
        SkillSet result = skillSetRepository.save(skillSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skillSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skill-sets : get all the skillSets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of skillSets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/skill-sets")
    @Timed
    public ResponseEntity<List<SkillSet>> getAllSkillSets(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SkillSets");
        Page<SkillSet> page = skillSetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/skill-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /skill-sets/:id : get the "id" skillSet.
     *
     * @param id the id of the skillSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillSet, or with status 404 (Not Found)
     */
    @GetMapping("/skill-sets/{id}")
    @Timed
    public ResponseEntity<SkillSet> getSkillSet(@PathVariable Long id) {
        log.debug("REST request to get SkillSet : {}", id);
        SkillSet skillSet = skillSetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skillSet));
    }

    /**
     * DELETE  /skill-sets/:id : delete the "id" skillSet.
     *
     * @param id the id of the skillSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skill-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkillSet(@PathVariable Long id) {
        log.debug("REST request to delete SkillSet : {}", id);
        skillSetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
