package hu.tsystems.devlad.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.tsystems.devlad.domain.SkillRequest;
import hu.tsystems.devlad.service.SkillRequestService;
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
 * REST controller for managing SkillRequest.
 */
@RestController
@RequestMapping("/api")
public class SkillRequestResource {

    private final Logger log = LoggerFactory.getLogger(SkillRequestResource.class);

    private static final String ENTITY_NAME = "skillRequest";
        
    private final SkillRequestService skillRequestService;

    public SkillRequestResource(SkillRequestService skillRequestService) {
        this.skillRequestService = skillRequestService;
    }

    /**
     * POST  /skill-requests : Create a new skillRequest.
     *
     * @param skillRequest the skillRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillRequest, or with status 400 (Bad Request) if the skillRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skill-requests")
    @Timed
    public ResponseEntity<SkillRequest> createSkillRequest(@Valid @RequestBody SkillRequest skillRequest) throws URISyntaxException {
        log.debug("REST request to save SkillRequest : {}", skillRequest);
        if (skillRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new skillRequest cannot already have an ID")).body(null);
        }
        SkillRequest result = skillRequestService.save(skillRequest);
        return ResponseEntity.created(new URI("/api/skill-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skill-requests : Updates an existing skillRequest.
     *
     * @param skillRequest the skillRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillRequest,
     * or with status 400 (Bad Request) if the skillRequest is not valid,
     * or with status 500 (Internal Server Error) if the skillRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skill-requests")
    @Timed
    public ResponseEntity<SkillRequest> updateSkillRequest(@Valid @RequestBody SkillRequest skillRequest) throws URISyntaxException {
        log.debug("REST request to update SkillRequest : {}", skillRequest);
        if (skillRequest.getId() == null) {
            return createSkillRequest(skillRequest);
        }
        SkillRequest result = skillRequestService.save(skillRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skillRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skill-requests : get all the skillRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of skillRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/skill-requests")
    @Timed
    public ResponseEntity<List<SkillRequest>> getAllSkillRequests(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SkillRequests");
        Page<SkillRequest> page = skillRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/skill-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /skill-requests/:id : get the "id" skillRequest.
     *
     * @param id the id of the skillRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillRequest, or with status 404 (Not Found)
     */
    @GetMapping("/skill-requests/{id}")
    @Timed
    public ResponseEntity<SkillRequest> getSkillRequest(@PathVariable Long id) {
        log.debug("REST request to get SkillRequest : {}", id);
        SkillRequest skillRequest = skillRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skillRequest));
    }

    /**
     * DELETE  /skill-requests/:id : delete the "id" skillRequest.
     *
     * @param id the id of the skillRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skill-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkillRequest(@PathVariable Long id) {
        log.debug("REST request to delete SkillRequest : {}", id);
        skillRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
