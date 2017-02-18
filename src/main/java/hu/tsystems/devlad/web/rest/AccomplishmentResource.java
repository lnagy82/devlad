package hu.tsystems.devlad.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.tsystems.devlad.domain.Accomplishment;

import hu.tsystems.devlad.repository.AccomplishmentRepository;
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
 * REST controller for managing Accomplishment.
 */
@RestController
@RequestMapping("/api")
public class AccomplishmentResource {

    private final Logger log = LoggerFactory.getLogger(AccomplishmentResource.class);

    private static final String ENTITY_NAME = "accomplishment";
        
    private final AccomplishmentRepository accomplishmentRepository;

    public AccomplishmentResource(AccomplishmentRepository accomplishmentRepository) {
        this.accomplishmentRepository = accomplishmentRepository;
    }

    /**
     * POST  /accomplishments : Create a new accomplishment.
     *
     * @param accomplishment the accomplishment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accomplishment, or with status 400 (Bad Request) if the accomplishment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accomplishments")
    @Timed
    public ResponseEntity<Accomplishment> createAccomplishment(@Valid @RequestBody Accomplishment accomplishment) throws URISyntaxException {
        log.debug("REST request to save Accomplishment : {}", accomplishment);
        if (accomplishment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accomplishment cannot already have an ID")).body(null);
        }
        Accomplishment result = accomplishmentRepository.save(accomplishment);
        return ResponseEntity.created(new URI("/api/accomplishments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accomplishments : Updates an existing accomplishment.
     *
     * @param accomplishment the accomplishment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accomplishment,
     * or with status 400 (Bad Request) if the accomplishment is not valid,
     * or with status 500 (Internal Server Error) if the accomplishment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accomplishments")
    @Timed
    public ResponseEntity<Accomplishment> updateAccomplishment(@Valid @RequestBody Accomplishment accomplishment) throws URISyntaxException {
        log.debug("REST request to update Accomplishment : {}", accomplishment);
        if (accomplishment.getId() == null) {
            return createAccomplishment(accomplishment);
        }
        Accomplishment result = accomplishmentRepository.save(accomplishment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accomplishment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accomplishments : get all the accomplishments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accomplishments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/accomplishments")
    @Timed
    public ResponseEntity<List<Accomplishment>> getAllAccomplishments(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Accomplishments");
        Page<Accomplishment> page = accomplishmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accomplishments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accomplishments/:id : get the "id" accomplishment.
     *
     * @param id the id of the accomplishment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accomplishment, or with status 404 (Not Found)
     */
    @GetMapping("/accomplishments/{id}")
    @Timed
    public ResponseEntity<Accomplishment> getAccomplishment(@PathVariable Long id) {
        log.debug("REST request to get Accomplishment : {}", id);
        Accomplishment accomplishment = accomplishmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accomplishment));
    }

    /**
     * DELETE  /accomplishments/:id : delete the "id" accomplishment.
     *
     * @param id the id of the accomplishment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accomplishments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccomplishment(@PathVariable Long id) {
        log.debug("REST request to delete Accomplishment : {}", id);
        accomplishmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
