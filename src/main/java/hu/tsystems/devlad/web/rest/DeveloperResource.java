package hu.tsystems.devlad.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.tsystems.devlad.domain.Developer;

import hu.tsystems.devlad.repository.DeveloperRepository;
import hu.tsystems.devlad.web.rest.util.HeaderUtil;
import hu.tsystems.devlad.web.rest.util.PaginationUtil;
import hu.tsystems.devlad.web.rest.util.ResponseUtil;
import hu.tsystems.devlad.service.dto.DeveloperDTO;
import hu.tsystems.devlad.service.mapper.DeveloperMapper;
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
 * REST controller for managing Developer.
 */
@RestController
@RequestMapping("/api")
public class DeveloperResource {

    private final Logger log = LoggerFactory.getLogger(DeveloperResource.class);

    private static final String ENTITY_NAME = "developer";
        
    private final DeveloperRepository developerRepository;

    private final DeveloperMapper developerMapper;

    public DeveloperResource(DeveloperRepository developerRepository, DeveloperMapper developerMapper) {
        this.developerRepository = developerRepository;
        this.developerMapper = developerMapper;
    }

    /**
     * POST  /developers : Create a new developer.
     *
     * @param developerDTO the developerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new developerDTO, or with status 400 (Bad Request) if the developer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/developers")
    @Timed
    public ResponseEntity<DeveloperDTO> createDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to save Developer : {}", developerDTO);
        if (developerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new developer cannot already have an ID")).body(null);
        }
        Developer developer = developerMapper.developerDTOToDeveloper(developerDTO);
        developer = developerRepository.save(developer);
        DeveloperDTO result = developerMapper.developerToDeveloperDTO(developer);
        return ResponseEntity.created(new URI("/api/developers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /developers : Updates an existing developer.
     *
     * @param developerDTO the developerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated developerDTO,
     * or with status 400 (Bad Request) if the developerDTO is not valid,
     * or with status 500 (Internal Server Error) if the developerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/developers")
    @Timed
    public ResponseEntity<DeveloperDTO> updateDeveloper(@Valid @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to update Developer : {}", developerDTO);
        if (developerDTO.getId() == null) {
            return createDeveloper(developerDTO);
        }
        Developer developer = developerMapper.developerDTOToDeveloper(developerDTO);
        developer = developerRepository.save(developer);
        DeveloperDTO result = developerMapper.developerToDeveloperDTO(developer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, developerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /developers : get all the developers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of developers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/developers")
    @Timed
    public ResponseEntity<List<DeveloperDTO>> getAllDevelopers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Developers");
        Page<Developer> page = developerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/developers");
        return new ResponseEntity<>(developerMapper.developersToDeveloperDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /developers/:id : get the "id" developer.
     *
     * @param id the id of the developerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the developerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/developers/{id}")
    @Timed
    public ResponseEntity<DeveloperDTO> getDeveloper(@PathVariable Long id) {
        log.debug("REST request to get Developer : {}", id);
        Developer developer = developerRepository.findOne(id);
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(developerDTO));
    }

    /**
     * DELETE  /developers/:id : delete the "id" developer.
     *
     * @param id the id of the developerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/developers/{id}")
    @Timed
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        log.debug("REST request to delete Developer : {}", id);
        developerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
